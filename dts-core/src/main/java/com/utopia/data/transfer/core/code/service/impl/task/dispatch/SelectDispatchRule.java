package com.utopia.data.transfer.core.code.service.impl.task.dispatch;

import com.utopia.data.transfer.core.code.model.EventDataTransaction;
import com.utopia.data.transfer.core.code.model.Message;
import com.utopia.data.transfer.core.code.service.impl.task.SelectTaskImpl;
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
    boolean start(SelectTaskImpl selectTask);

    /**
     *
     * @param pipeline id
     * @param message
     * @return
     */
    CompletableFuture<UtopiaResponseModel> dispatch(Pipeline pipeline, Message<EventDataTransaction> message);
}
