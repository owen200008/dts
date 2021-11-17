package com.utopia.data.transfer.core.extension.base.dispatch;

import com.utopia.data.transfer.model.code.entity.Task;
import com.utopia.data.transfer.model.code.entity.data.EventDataTransaction;
import com.utopia.data.transfer.model.code.entity.data.Message;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.model.rsp.UtopiaResponseModel;

import java.util.concurrent.CompletableFuture;

/**
 * @author owen.cai
 * @create_date 2021/9/30
 * @alter_author
 * @alter_date
 */
public interface SelectDispatchRule {
    /**
     * 初始化
     * @param selectTask
     */
    boolean start(Task selectTask);

    /**
     *
     * @param pipeline id
     * @param message
     * @return
     */
    CompletableFuture<UtopiaResponseModel> dispatch(Pipeline pipeline, Message<EventDataTransaction> message);
}
