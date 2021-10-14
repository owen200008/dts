package com.utopia.data.transfer.core.code.service.impl.task.load;

import com.utopia.data.transfer.model.code.transfer.TransferData;
import com.utopia.model.rsp.UtopiaResponseModel;

import java.util.concurrent.CompletableFuture;

/**
 * @author owen.cai
 * @create_date 2021/10/12
 * @alter_author
 * @alter_date
 */
public interface LoadTransferFacade {
    /**
     * 传输
     * @return
     */
    CompletableFuture<UtopiaResponseModel> transfer(TransferData transferData);
}
