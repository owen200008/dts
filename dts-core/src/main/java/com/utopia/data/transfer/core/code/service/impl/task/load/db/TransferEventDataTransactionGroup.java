package com.utopia.data.transfer.core.code.service.impl.task.load.db;

import com.utopia.data.transfer.model.code.transfer.EventDataTransactionInterface;
import lombok.Data;

import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/10/15
 * @alter_author
 * @alter_date
 */
@Data
public class TransferEventDataTransactionGroup {
    private boolean isDdl;
    private List<EventDataTransactionInterface> ayList;
}
