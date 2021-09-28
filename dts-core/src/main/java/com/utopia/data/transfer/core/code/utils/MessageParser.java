package com.utopia.data.transfer.core.code.utils;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.utopia.data.transfer.core.archetype.base.ServiceException;
import com.utopia.data.transfer.core.code.base.ErrorCode;
import com.utopia.data.transfer.core.code.bean.DataMedia;
import com.utopia.data.transfer.core.code.bean.DataMediaPair;
import com.utopia.data.transfer.core.code.bean.Pipeline;
import com.utopia.data.transfer.core.code.src.dialect.DbDialect;
import com.utopia.data.transfer.core.code.model.EventData;
import com.utopia.data.transfer.core.code.model.EventType;
import com.utopia.data.transfer.core.code.service.ConfigService;
import com.utopia.data.transfer.core.code.src.dialect.DbDialectFactory;
import com.utopia.data.transfer.core.code.base.datasource.bean.db.DbMediaSource;
import com.utopia.data.transfer.core.code.src.model.EventColumn;
import com.utopia.utils.CollectionUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ddlutils.model.Table;

import java.util.*;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
@Slf4j
public class MessageParser {

    @Setter
    private ConfigService configService;
    @Setter
    private DbDialectFactory dbDialectFactory;

    private static final String compatibleMarkTable            = "retl_client";

    public List<EventData> parse(Long pipelineId, List<CanalEntry.Entry> datas) throws ServiceException {
        List<EventData> eventDatas = new ArrayList<EventData>();
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

    protected void parseTransactionEnd(List<CanalEntry.Entry> transactionDataBuffer, Pipeline pipeline, List<EventData> eventDatas){
        for (CanalEntry.Entry bufferEntry : transactionDataBuffer) {
            List<EventData> parseDatas = internParse(pipeline, bufferEntry);
            if (CollectionUtils.isEmpty(parseDatas)) {
                // 可能为空，针对ddl返回时就为null
                continue;
            }

            // 初步计算一下事件大小
            long totalSize = bufferEntry.getHeader().getEventLength();
            long eachSize = totalSize / parseDatas.size();
            for (EventData eventData : parseDatas) {
                if (eventData == null) {
                    continue;
                }
                // 记录一下大小
                eventData.setSize(eachSize);
                eventDatas.add(eventData);
            }
        }
    }

    private List<EventData> internParse(Pipeline pipeline, CanalEntry.Entry entry) {
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

        if (eventType.isDdl()) {
            boolean notExistReturnNull = false;
            if (eventType.isRename()) {
                notExistReturnNull = true;
            }

            DataMedia dataMedia = ConfigHelper.findSourceDataMedia(pipeline,
                    schemaName,
                    tableName,
                    notExistReturnNull);
            // 如果EventType是CREATE/ALTER，需要reload
            // DataMediaInfo;并且把CREATE/ALTER类型的事件丢弃掉.
            if (dataMedia != null && (eventType.isCreate() || eventType.isAlter() || eventType.isRename())) {
                DbDialect dbDialect = dbDialectFactory.getDbDialect(pipeline.getId(),
                        (DbMediaSource) dataMedia.getSource());
                dbDialect.reloadTable(schemaName, tableName);// 更新下meta信息
            }

            // 处理下ddl操作
            EventData eventData = new EventData();
            eventData.setSchemaName(schemaName);
            eventData.setTableName(tableName);
            eventData.setEventType(eventType);
            eventData.setExecuteTime(entry.getHeader().getExecuteTime());
            eventData.setSql(rowChange.getSql());
            eventData.setDdlSchemaName(rowChange.getDdlSchemaName());
            eventData.setTableId(dataMedia.getId());
            return Arrays.asList(eventData);
        }

        List<EventData> eventDatas = new ArrayList<EventData>();
        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
            EventData eventData = internParse(pipeline, entry, rowChange, rowData);
            if (eventData != null) {
                eventDatas.add(eventData);
            }
        }

        return eventDatas;
    }

    private EventData internParse(Pipeline pipeline, CanalEntry.Entry entry, CanalEntry.RowChange rowChange, CanalEntry.RowData rowData) {
        EventData eventData = new EventData();
        eventData.setTableName(entry.getHeader().getTableName());
        eventData.setSchemaName(entry.getHeader().getSchemaName());
        eventData.setEventType(EventType.valueOf(rowChange.getEventType().name()));
        eventData.setExecuteTime(entry.getHeader().getExecuteTime());
        EventType eventType = eventData.getEventType();

        Table table = null;
        DataMediaPair dataMediaPair = ConfigHelper.findDataMediaPairBySourceName(pipeline,
                eventData.getSchemaName(),
                eventData.getTableName());
        DataMedia dataMedia = dataMediaPair.getSource();
        eventData.setTableId(dataMedia.getId());
        // 获取目标表
        DataMedia targetDataMedia = dataMediaPair.getTarget();

        List<CanalEntry.Column> beforeColumns = rowData.getBeforeColumnsList();
        List<CanalEntry.Column> afterColumns = rowData.getAfterColumnsList();
        String tableName = eventData.getSchemaName() + "." + eventData.getTableName();

        // 判断一下是否需要all columns
        // 如果是rowMode模式，所有字段都需要标记为updated
        boolean isRowMode = false;
        boolean needAllColumns = false;

        // 变更后的主键
        Map<String, EventColumn> keyColumns = new LinkedHashMap<String, EventColumn>();
        // 变更前的主键
        Map<String, EventColumn> oldKeyColumns = new LinkedHashMap<String, EventColumn>();
        // 有变化的非主键
        Map<String, EventColumn> notKeyColumns = new LinkedHashMap<String, EventColumn>();

        if (eventType.isInsert()) {
            for (CanalEntry.Column column : afterColumns) {
                if (isKey(tableName, column)) {
                    keyColumns.put(column.getName(), copyEventColumn(column, true));
                } else {
                    // mysql 有效
                    notKeyColumns.put(column.getName(), copyEventColumn(column, true));
                }
            }
        } else if (eventType.isDelete()) {
            for (CanalEntry.Column column : beforeColumns) {
                if (isKey(tableName, column)) {
                    keyColumns.put(column.getName(), copyEventColumn(column, true));
                } else {
                    // mysql 有效
                    notKeyColumns.put(column.getName(), copyEventColumn(column, true));
                }
            }
        } else if (eventType.isUpdate()) {
            // 获取变更前的主键.
            for (CanalEntry.Column column : beforeColumns) {
                if (isKey(tableName, column)) {
                    oldKeyColumns.put(column.getName(), copyEventColumn(column, true));
                    // 同时记录一下new
                    // key,因为mysql5.6之后出现了minimal模式,after里会没有主键信息,需要在before记录中找
                    keyColumns.put(column.getName(), copyEventColumn(column, true));
                } else {
                    if (needAllColumns && entry.getHeader().getSourceType() == CanalEntry.Type.ORACLE) {
                        // 针对行记录同步时，针对oracle记录一下非主键的字段，因为update时针对未变更的字段在aftercolume里没有
                        notKeyColumns.put(column.getName(), copyEventColumn(column, isRowMode));
                    }
                }
            }
            for (CanalEntry.Column column : afterColumns) {
                if (isKey(tableName, column)) {
                    // 获取变更后的主键
                    keyColumns.put(column.getName(), copyEventColumn(column, true));
                } else if (needAllColumns || entry.getHeader().getSourceType() == CanalEntry.Type.ORACLE
                        || column.getUpdated()) {
                    // 在update操作时，oracle和mysql存放变更的非主键值的方式不同,oracle只有变更的字段;
                    // mysql会把变更前和变更后的字段都发出来，只需要取有变更的字段.
                    // 如果是oracle库，after里一定为对应的变更字段

                    boolean isUpdate = true;
                    if (entry.getHeader().getSourceType() == CanalEntry.Type.MYSQL) { // mysql的after里部分数据为未变更,oracle里after里为变更字段
                        isUpdate = column.getUpdated();
                    }

                    notKeyColumns.put(column.getName(), copyEventColumn(column, isRowMode || isUpdate));// 如果是rowMode，所有字段都为updated
                }
            }
        }

        List<EventColumn> keys = new ArrayList<EventColumn>(keyColumns.values());
        List<EventColumn> oldKeys = new ArrayList<EventColumn>(oldKeyColumns.values());
        List<EventColumn> columns = new ArrayList<EventColumn>(notKeyColumns.values());

        Collections.sort(keys, new EventColumnIndexComparable());
        Collections.sort(oldKeys, new EventColumnIndexComparable());
        Collections.sort(columns, new EventColumnIndexComparable());
        if (!keyColumns.isEmpty()) {
            eventData.setKeys(keys);
            if (eventData.getEventType().isUpdate() && !oldKeys.equals(keys)) { // update类型，如果存在主键不同,则记录下old
                // keys为变更前的主键
                eventData.setOldKeys(oldKeys);
            }
            eventData.setColumns(columns);
            // } else if (CanalEntry.Type.MYSQL ==
            // entry.getHeader().getSourceType()) {
            // // 只支持mysql无主键同步
            // if (eventType.isUpdate()) {
            // List<EventColumn> oldColumns = new ArrayList<EventColumn>();
            // List<EventColumn> newColumns = new ArrayList<EventColumn>();
            // for (Column column : beforeColumns) {
            // oldColumns.add(copyEventColumn(column, true, tableHolder));
            // }
            //
            // for (Column column : afterColumns) {
            // newColumns.add(copyEventColumn(column, true, tableHolder));
            // }
            // Collections.sort(oldColumns, new EventColumnIndexComparable());
            // Collections.sort(newColumns, new EventColumnIndexComparable());
            // eventData.setOldKeys(oldColumns);// 做为老主键
            // eventData.setKeys(newColumns);// 做为新主键，需要保证新老主键字段数量一致
            // } else {
            // // 针对无主键，等同为所有都是主键进行处理
            // eventData.setKeys(columns);
            // }
        } else {
            throw new ServiceException(ErrorCode.DTS_KEY_COLUMN_NOFIND.getCode(), "this rowdata has no pks , entry: " + entry.toString() + " and rowData: "
                    + rowData);
        }

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
