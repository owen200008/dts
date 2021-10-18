package com.utopia.data.transfer.core.code.service.impl.task;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.Getter;

/**
 * @author owen.cai
 * @create_date 2021/10/18
 * @alter_author
 * @alter_date
 */
public class SelectTaskPrometheus {

    @Getter
    private Counter selectTimes;
    @Getter
    private Counter selectDataTimes;
    @Getter
    private Counter callCounter;
    @Getter
    private Counter timerException;
    @Getter
    private Counter dispathException;
    @Getter
    private Counter runTimeException;
    @Getter
    private Counter rollbackException;


    public SelectTaskPrometheus(Long pipelineId){
        this.selectTimes = Metrics.counter("select_times", getTags(pipelineId));
        this.selectDataTimes = Metrics.counter("select_data_times", getTags(pipelineId));
        this.callCounter = Metrics.counter("select_call_counter", getTags(pipelineId));
        this.timerException = Metrics.counter("select_timer_exception_counter", getTags(pipelineId));
        this.dispathException = Metrics.counter("select_dispatch_exception_counter", getTags(pipelineId));
        this.runTimeException = Metrics.counter("select_runtime_exception_counter", getTags(pipelineId));
        this.rollbackException = Metrics.counter("select_rollback_exception", getTags(pipelineId));
    }

    public Iterable<Tag> getTags(Long pipelineId) {
        return Tags.of(Tag.of("pipelineId", pipelineId.toString()));
    }
}
