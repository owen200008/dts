package com.utopia.data.transfer.core.code.service;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
public interface Task {

    /**
     * 开始
     * @param pipelineId
     * @return
     */
    boolean startTask(Long pipelineId);

    /**
     * 关闭
     */
    void shutdown();

    /**
     * 等待关闭
     */
    void waitClose();
}
