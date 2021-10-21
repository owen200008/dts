package com.utopia.data.transfer.core.code.service.impl.task.load;

import com.utopia.data.transfer.core.code.service.impl.task.LoadTaskImpl;

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
    void start(LoadTaskImpl loadTask);

    /**
     *
     */
    void stop();
}
