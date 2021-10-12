package com.utopia.data.transfer.core.code.service;

import com.utopia.extension.UtopiaSPI;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
@UtopiaSPI
public interface Task {

    /**
     * 开始
     */
    void startTask(Long pipelineId);

    /**
     * 关闭
     */
    void shutdown();

    /**
     * 等待关闭
     */
    void waitClose();
}
