package com.utopia.data.transfer.core.extension.select.utils;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.utopia.data.transfer.core.base.config.ConfigService;
import com.utopia.data.transfer.core.extension.base.dialect.DbDialect;
import com.utopia.data.transfer.core.extension.base.dialect.DbDialectFactory;
import com.utopia.data.transfer.core.extension.tools.utils.ConfigHelper;
import com.utopia.data.transfer.core.extension.tools.utils.EventColumnIndexComparable;
import com.utopia.data.transfer.model.archetype.ServiceException;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.model.code.entity.data.EventDataTransaction;
import com.utopia.data.transfer.model.code.data.media.DataMediaRuleSource;
import com.utopia.data.transfer.model.code.data.media.DataMediaRulePair;
import com.utopia.data.transfer.model.code.entity.EventColumn;
import com.utopia.data.transfer.model.code.event.EventType;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.data.transfer.model.code.entity.data.EventData;
import com.utopia.data.transfer.model.code.transfer.TransferUniqueDesc;
import com.utopia.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
@Slf4j
@Service
public class MessageParser {

    @Autowired
    private ConfigService configService;
    @Autowired
    private DbDialectFactory dbDialectFactory;

    public List<EventDataTransaction> parse(Long pipelineId, List<CanalEntry.Entry> datas) throws ServiceException {
        List<EventDataTransaction> eventDatas = new ArrayList();
        Pipeline pipeline = configService.getPipeline(pipelineId);
        List<CanalEntry.Entry> transactionDataBuffer = new ArrayList<CanalEntry.Entry>();
        // hz为主站点，us->hz的数据，需要回环同步会us。并且需要开启回环补救算法

        try {
            for (CanalEntry.Entry entry : datas) {
                switch (entry.getEntryType()) {
                    case TRANSACTIONBEGIN:
                        break;
                    case ROWDATA:
                        transactionDataBuffer.add(entry);
                        break;
                    case TRANSACTIONEND:
                        // 添加数据解析
                        parseTransactionEnd(transactionDataBuffer, pipeline, eventDatas);

                        transactionDataBuffer.clear();
                        break;
                    default:
                        break;
                }
            }
            // 添加最后一次的数据，可能没有TRANSACTIONEND
            // 添加数据解析
            parseTransactionEnd(transactionDataBuffer, pipeline, eventDatas);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.CANAL_PARSE_DATA_ERROR, e);
        }

        return eventDatas;
    }

    protected void parseTransactionEnd(List<CanalEntry.Entry> transactionDataBuffer, Pipeline pipeline, List<EventDataTransaction> eventDatas) {
        EventDataTransaction lastEventData = null;
        for (CanalEntry.Entry bufferEntry : transactionDataBuffer) {
            EventDataTransaction eventDataTransaction = internParse(pipeline, bufferEntry);
            if (eventDataTransaction == null || CollectionUtils.isEmpty(eventDataTransaction.getDatas())) {
                // 可能为空，针对ddl返回时就为null
                continue;
            }
            //需要判断是否和上一个gtid相同，如果相同
            if(lastEventData == null) {
                lastEventData = eventDataTransaction;
                eventDatas.add(eventDataTransaction);
            }
            else {
                if(lastEventData.getGtid().isSame(eventDataTransaction.getGtid())){
                    lastEventData.getDatas().addAll(eventDataTransaction.getDatas());
                }
                else{
                    lastEventData = eventDataTransaction;
                    eventDatas.add(eventDataTransaction);
                }
            }
        }
    }

    private EventDataTransaction internParse(Pipeline pipeline, CanalEntry.Entry entry) {
        CanalEntry.RowChange rowChange = null;
        try {
            rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.CANAL_PARSE_DATA_ROW_ERROR.getCode(), "parser of canal-event has an error , data:" + entry.toString(), e);
        }

        if (rowChange == null) {
            return null;
        }

        String schemaName = entry.getHeader().getSchemaName();
        String tableName = entry.getHeader().getTableName();
        EventType eventType = EventType.valueOf(rowChange.getEventType().name());

        // 处理下DDL操作
        if (eventType.isQuery()) {
            // 直接忽略query事件
            return null;
        }

        TransferUniqueDesc eventDataUniqueDesc = TransferUniqueDesc.parseGtid(entry.getHeader().getGtid());
        if(Objects.isNull(eventDataUniqueDesc)){
            throw new ServiceException(ErrorCode.CANAL_PARSE_GTID_ERROR.getCode(), "parser of canal-event has an error , data:" + entry.toString());
        }

        EventDataTransaction ret = new EventDataTransaction(eventDataUniqueDesc);

        if (eventType.isDdl()) {
            boolean notExistReturnNull = false;
            if (eventType.isRename()) {
                notExistReturnNull = true;
            }

            DataMediaRuleSource dataMedia = ConfigHelper.findSourceDataMedia(pipeline,
                    schemaName,
                    tableName,
                    notExistReturnNull);
            // 如果EventType是CREATE/ALTER，需要reload
            // DataMediaInfo;并且把CREATE/ALTER类型的事件丢弃掉.
            if (dataMedia != null && (eventType.isCreate() || eventType.isAlter() || eventType.isRename())) {
                DbDialect dbDialect = dbDialectFactory.getDbDialect(pipeline.getId(), configService.getEntityDesc(pipeline.getSourceEntityId()));
                // 更新下meta信息
                dbDialect.reloadTable(schemaName, tableName);
            }

            // 处理下ddl操作
            EventData eventData = new EventData();
            eventData.setEventType(eventType);
            eventData.setExecuteTime(entry.getHeader().getExecuteTime());
            eventData.setSql(rowChange.getSql());
            eventData.setDdlSchemaName(rowChange.getDdlSchemaName());
            eventData.setTableId(dataMedia.getId());

            ret.setDatas(Arrays.asList(eventData));
            return ret;
        }

        List<EventData> eventDatas = new ArrayList<EventData>();
        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
            EventData eventData = internParse(pipeline, entry, rowChange, rowData);
            if (eventData != null) {
                eventDatas.add(eventData);
            }
        }

        ret.setDatas(eventDatas);
        return ret;
    }

    private EventData internParse(Pipeline pipeline, CanalEntry.Entry entry, CanalEntry.RowChange rowChange, CanalEntry.RowData rowData) {
        String onlyTableName = entry.getHeader().getTableName();
        String schemaName = entry.getHeader().getSchemaName();
        EventData eventData = new EventData();
        eventData.setEventType(EventType.valueOf(rowChange.getEventType().name()));
        eventData.setExecuteTime(entry.getHeader().getExecuteTime());
        EventType eventType = eventData.getEventType();

        DataMediaRulePair dataMediaPair = ConfigHelper.findDataMediaPairBySourceName(pipeline,
                schemaName,
                onlyTableName);
        DataMediaRuleSource dataMedia = dataMediaPair.getSource();
        eventData.setTableId(dataMedia.getId());

        List<CanalEntry.Column> beforeColumns = rowData.getBeforeColumnsList();
        List<CanalEntry.Column> afterColumns = rowData.getAfterColumnsList();
        String tableName = schemaName + "." + onlyTableName;

        // 变更后的主键
        Map<String, EventColumn> keyColumns = new LinkedHashMap<String, EventColumn>();
        // 变更前的主键
        Map<String, EventColumn> oldKeyColumns = new LinkedHashMap<String, EventColumn>();
        // 有变化的非主键
        Map<String, EventColumn> notKeyColumns = new LinkedHashMap<String, EventColumn>();
        //所有键
        Map<String, EventColumn> allColumns = new LinkedHashMap<String, EventColumn>();

        if (eventType.isInsert()) {
            for (CanalEntry.Column column : afterColumns) {
                EventColumn eventColumn = copyEventColumn(column, true);
                allColumns.put(column.getName(), eventColumn);
                if (isKey(tableName, column)) {
                    keyColumns.put(column.getName(), eventColumn);
                } else {
                    // mysql 有效
                    notKeyColumns.put(column.getName(), eventColumn);
                }
            }
        } else if (eventType.isDelete()) {
            for (CanalEntry.Column column : beforeColumns) {
                EventColumn eventColumn = copyEventColumn(column, true);
                allColumns.put(column.getName(), eventColumn);
                if (isKey(tableName, column)) {
                    keyColumns.put(column.getName(), eventColumn);
                } else {
                    // mysql 有效
                    notKeyColumns.put(column.getName(), eventColumn);
                }
            }
        } else if (eventType.isUpdate()) {
            // 获取变更前的主键.
            for (CanalEntry.Column column : beforeColumns) {
                if (isKey(tableName, column)) {
                    EventColumn eventColumn = copyEventColumn(column, true);
                    allColumns.put(column.getName(), eventColumn);

                    oldKeyColumns.put(column.getName(), eventColumn);
                    // 同时记录一下new
                    // key,因为mysql5.6之后出现了minimal模式,after里会没有主键信息,需要在before记录中找
                    keyColumns.put(column.getName(), eventColumn);
                }
            }
            for (CanalEntry.Column column : afterColumns) {
                if (isKey(tableName, column)) {
                    EventColumn eventColumn = copyEventColumn(column, true);
                    allColumns.put(column.getName(), eventColumn);
                    // 获取变更后的主键
                    keyColumns.put(column.getName(), eventColumn);
                } else if (column.getUpdated()) {
                    EventColumn eventColumn = copyEventColumn(column, true);
                    allColumns.put(column.getName(), eventColumn);
                    // 在update操作时，oracle和mysql存放变更的非主键值的方式不同,oracle只有变更的字段;
                    // mysql会把变更前和变更后的字段都发出来，只需要取有变更的字段.
                    notKeyColumns.put(column.getName(), eventColumn);
                }
                else{
                    EventColumn eventColumn = copyEventColumn(column, false);
                    allColumns.put(column.getName(), eventColumn);
                }
            }
        }

        //更新和删除需要有主键
        if(eventType.isUpdate()){
            if(keyColumns.isEmpty()) {
                throw new ServiceException(ErrorCode.DTS_KEY_COLUMN_NOFIND.getCode(), "this update rowdata has no pks , entry: " + entry.toString() + " and rowData: "
                        + rowData);
            }
        }
        List<EventColumn> keys = new ArrayList<EventColumn>(keyColumns.values());
        List<EventColumn> oldKeys = new ArrayList<EventColumn>(oldKeyColumns.values());
        List<EventColumn> columns = new ArrayList<EventColumn>(notKeyColumns.values());

        Collections.sort(keys, new EventColumnIndexComparable());
        Collections.sort(oldKeys, new EventColumnIndexComparable());
        Collections.sort(columns, new EventColumnIndexComparable());

        eventData.setKeys(keys);
        if (eventData.getEventType().isUpdate() && !oldKeys.equals(keys)) {
            // update类型，如果存在主键不同,则记录下old
            // keys为变更前的主键
            eventData.setOldKeys(oldKeys);
        }
        eventData.setColumns(columns);
        eventData.setAllColumns(allColumns);
        return eventData;
    }

    private boolean isKey(String tableName, CanalEntry.Column column) {
        return column.getIsKey();
    }


    /**
     * 把 erosa-protocol's Column 转化成 otter's model EventColumn.
     *
     * @param column
     * @return
     */
    private EventColumn copyEventColumn(CanalEntry.Column column, boolean isUpdate) {
        EventColumn eventColumn = new EventColumn();
        eventColumn.setIndex(column.getIndex());
        eventColumn.setKey(column.getIsKey());
        eventColumn.setNull(column.getIsNull());
        eventColumn.setColumnName(column.getName());
        eventColumn.setColumnValue(column.getValue());
        eventColumn.setUpdate(isUpdate);
        eventColumn.setColumnType(column.getSqlType());
        return eventColumn;
    }

}
