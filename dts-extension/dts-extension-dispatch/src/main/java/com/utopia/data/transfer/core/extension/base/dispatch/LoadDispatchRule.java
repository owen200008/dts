package com.utopia.data.transfer.core.extension.base.dispatch;

import com.utopia.data.transfer.model.code.entity.Task;

/**
 * @author owen.cai
 * @create_date 2021/10/21
 * @alter_author
 * @alter_date
 */
public interface LoadDispatchRule {

    /**
     *
     * @param loadTask
     */
    void start(Task loadTask, LoadTransferFacade loadTransferFacade);

    /**
     *
     */
    void stop();
}