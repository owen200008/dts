package com.utopia.data.transfer.model.code.entity.data;

import com.utopia.data.transfer.model.code.transfer.EventDataTransactionInterface;
import com.utopia.data.transfer.model.code.transfer.TransferUniqueDesc;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
@Data
public class EventDataTransaction implements EventDataTransactionInterface, Serializable {

    private TransferUniqueDesc                  gtid;
    private List<EventData>                     datas;

    public EventDataTransaction(TransferUniqueDesc gtid) {
        this.gtid = gtid;
    }
}
