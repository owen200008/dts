package com.utopia.data.transfer.model.code.transfer;

import com.utopia.data.transfer.model.code.entity.EventColumn;
import com.utopia.data.transfer.model.code.event.EventType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/10/12
 * @alter_author
 * @alter_date
 */
@Data
public class TransferEventData implements Serializable {
    /**
     * dts内部维护的一套tableId，与manager中得到的table Id对应
     */
    private long                tableId          = -1;

    /**
     * 变更数据的业务类型(I/U/D/C/A/E),与canal中的EntryProtocol中定义的EventType一致.
     */
    private EventType           eventType;

    /**
     * 变更数据的业务时间.
     */
    private long                executeTime;


    /**
     * 变更前的主键值,如果是insert/delete变更前和变更后的主键值是一样的.
     */
    private List<EventColumn> oldKeys;

    /**
     * 变更后的主键值,如果是insert/delete变更前和变更后的主键值是一样的.
     */
    private List<EventColumn> keys;

    /**
     * 非主键的其他字段
     */
    private List<EventColumn> columns;

    /**
     * 当eventType =
     * CREATE/ALTER/ERASE时，就是对应的sql语句，其他情况为动态生成的INSERT/UPDATE/DELETE sql
     */
    private String            sql;

    /**
     * ddl/query的schemaName，会存在跨库ddl，需要保留执行ddl的当前schemaName
     */
    private String            ddlSchemaName;

    public List<EventColumn> calcUpdatedColumns() {
        List<EventColumn> columns = new ArrayList();
        for (EventColumn column : this.columns) {
            if (column.isUpdate()) {
                columns.add(column);
            }
        }
        return columns;
    }
}
