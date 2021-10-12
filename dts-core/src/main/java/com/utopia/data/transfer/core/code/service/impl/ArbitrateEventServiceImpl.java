package com.utopia.data.transfer.core.code.service.impl;

import com.utopia.data.transfer.core.code.service.ArbitrateEventService;
import com.utopia.data.transfer.core.code.service.impl.event.PipelineEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
@Service
public class ArbitrateEventServiceImpl implements ArbitrateEventService {

    @Autowired
    private PipelineEventService pipelineEventService;

    @Override
    public PipelineEventService getPipelineEventService() {
        return pipelineEventService;
    }

    @Override
    public void closePipeline(Long pipelineId) {
        pipelineEventService.closePipeline(pipelineId);
    }
}
