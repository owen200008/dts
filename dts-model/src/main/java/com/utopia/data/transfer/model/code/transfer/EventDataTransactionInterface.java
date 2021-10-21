package com.utopia.data.transfer.model.code.transfer;

import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/10/21
 * @alter_author
 * @alter_date
 */
public interface EventDataTransactionInterface {

    TransferUniqueDesc getGtid();

    /**
     * 获取数据
     * @return
     */
    List<? extends EventDataInterface> getDatas();


}
