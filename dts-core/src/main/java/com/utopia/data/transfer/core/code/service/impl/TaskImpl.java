package com.utopia.data.transfer.core.code.service.impl;

import com.utopia.data.transfer.core.code.service.ArbitrateEventService;
import com.utopia.data.transfer.core.code.service.ConfigService;
import com.utopia.data.transfer.core.code.service.MessageParser;
import com.utopia.data.transfer.core.code.service.Task;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.extension.UtopiaSPIInject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
@Slf4j
public abstract class TaskImpl implements Runnable, Task {

    @UtopiaSPIInject
    protected ConfigService configService;
    @UtopiaSPIInject
    protected MessageParser messageParser;
    @UtopiaSPIInject
    protected ArbitrateEventService arbitrateEventService;

    protected Thread                    thread = new Thread(this);
    protected Long                      pipelineId;
    protected Pipeline                  pipeline;



    protected volatile boolean          running = true;
    protected Map<Long, Future>         pendingFuture = new HashMap();

    protected String createTaskName(long pipelineId, String taskName) {
        return new StringBuilder().append("pipelineId = ").append(pipelineId).append(",taskName = ").append(taskName).toString();
    }

    @Override
    public void startTask(Long pipelineId) {
        this.pipelineId = pipelineId;
        this.pipeline = configService.getPipeline(pipelineId);

        thread.setName(createTaskName(pipelineId, ClassUtils.getShortClassName(this.getClass())));

        thread.start();
    }

    @Override
    public void shutdown() {
        running = false;
        thread.interrupt();

        List<Future> cancelFutures = new ArrayList();
        for (Map.Entry<Long, Future> entry : pendingFuture.entrySet()) {
            if (!entry.getValue().isDone()) {
                log.warn("WARN ## Task future processId[{}] canceled!", entry.getKey());
                cancelFutures.add(entry.getValue());
            }
        }

        for (Future future : cancelFutures) {
            future.cancel(true);
        }
        pendingFuture.clear();
    }

    @Override
    public void waitClose() {
        try {
            //最大等待5s
            thread.join(5000);
        } catch (InterruptedException e) {
            log.error("thread exception", e);
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
