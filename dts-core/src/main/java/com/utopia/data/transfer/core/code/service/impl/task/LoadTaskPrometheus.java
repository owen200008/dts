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
public class LoadTaskPrometheus {

    @Getter
    private Counter loadError;
    @Getter
    private Counter loadException;


    public LoadTaskPrometheus(Long pipelineId){
        this.loadError = Metrics.counter("load_error", getTags(pipelineId));
        this.loadException = Metrics.counter("load_exception", getTags(pipelineId));
    }

    public Iterable<Tag> getTags(Long pipelineId) {
        return Tags.of(Tag.of("pipelineId", pipelineId.toString()));
    }
}
