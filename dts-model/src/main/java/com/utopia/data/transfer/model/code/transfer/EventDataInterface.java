package com.utopia.data.transfer.model.code.transfer;

import com.utopia.data.transfer.model.code.entity.EventColumn;
import com.utopia.data.transfer.model.code.event.EventType;

import java.util.Collection;
import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/10/21
 * @alter_author
 * @alter_date
 */
public interface EventDataInterface {
    /**
     * 获取表id
     * @return
     */
    long getTableId();

    /**
     *
     * @return
     */
    EventType getEventType();

    /**
     *
     * @return
     */
    CharSequence getDdlSchemaName();

    /**
     *
     * @return
     */
    String getSql();

    /**
     *
     * @return
     */
    List<EventColumn> getOldKeys();

    /**
     *
     * @return
     */
    List<EventColumn> getKeys();

    /**
     *
     * @return
     */
    List<EventColumn> getColumns();

}
