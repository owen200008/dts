package com.utopia.data.transfer.core.code.service;

import com.utopia.data.transfer.core.code.service.impl.event.PipelineEventService;

/**
 * 仲裁事件
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
public interface ArbitrateEventService {

    /**
     * pipeline 状态
     * @return
     */
    PipelineEventService getPipelineEventService();

    /**
     * 关闭一个pipeline
     * @param pipelineId
     */
    void closePipeline(Long pipelineId);
}
