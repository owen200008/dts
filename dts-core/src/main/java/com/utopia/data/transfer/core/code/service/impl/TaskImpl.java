package com.utopia.data.transfer.core.code.service.impl;

import com.utopia.data.transfer.core.code.service.ArbitrateEventService;
import com.utopia.data.transfer.core.code.service.ConfigService;
import com.utopia.data.transfer.core.code.service.MessageParser;
import com.utopia.data.transfer.core.code.service.Task;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.extension.UtopiaSPIInject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
@Slf4j
public abstract class TaskImpl implements Runnable, Task {

    protected final ConfigService configService;
    protected final ArbitrateEventService arbitrateEventService;

    protected Thread                    thread = new Thread(this);
    @Getter
    protected Long                      pipelineId;
    @Getter
    protected Pipeline                  pipeline;
    @Getter
    protected EntityDesc                sourceEntityDesc;
    @Getter
    protected EntityDesc                targetEntityDesc;

    protected volatile boolean          running = true;

    public TaskImpl(ConfigService configService, ArbitrateEventService arbitrateEventService){
        this.configService = configService;
        this.arbitrateEventService = arbitrateEventService;
    }

    protected String createTaskName(long pipelineId, String taskName) {
        return new StringBuilder().append("pipelineId = ").append(pipelineId).append(",taskName = ").append(taskName).toString();
    }

    @Override
    public void startTask(Long pipelineId) {
        this.pipelineId = pipelineId;
        this.pipeline = configService.getPipeline(pipelineId);
        this.sourceEntityDesc = configService.getEntityDesc(this.pipeline.getSourceEntityId());
        this.targetEntityDesc = configService.getEntityDesc(this.pipeline.getTargetEntityId());

        thread.setName(createTaskName(pipelineId, ClassUtils.getShortClassName(this.getClass())));

        thread.start();
    }

    @Override
    public void shutdown() {
        running = false;
    }

    @Override
    public void waitClose() {
        try {
            //最大等待10s
            thread.join(10000);
        } catch (InterruptedException e) {
            thread.interrupt();
            log.error("thread exception interrupt", e);
        }
    }

    protected boolean isInterrupt(Throwable e) {
        if (!running) {
            return true;
        }
        if (e instanceof InterruptedException) {
            return true;
        }
        if (ExceptionUtils.getRootCause(e) instanceof InterruptedException) {
            return true;
        }
        return false;
    }


}
