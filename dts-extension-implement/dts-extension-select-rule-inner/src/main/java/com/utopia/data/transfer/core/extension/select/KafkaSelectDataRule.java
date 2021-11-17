package com.utopia.data.transfer.core.extension.select;

import com.utopia.data.transfer.core.base.config.ConfigService;
import com.utopia.data.transfer.core.extension.select.kafka.KafkaSelector;
import com.utopia.data.transfer.model.code.entity.data.EventDataTransaction;
import com.utopia.data.transfer.model.code.entity.data.Message;
import com.utopia.data.transfer.core.extension.base.select.SelectDataRule;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author owen.cai
 * @create_date 2021/10/19
 * @alter_author
 * @alter_date
 */
@Slf4j
public class KafkaSelectDataRule implements SelectDataRule {

    private KafkaSelector kafkaSelector;
    void init(Long pipelineId, ConfigService configService){
        Pipeline pipeline = configService.getPipeline(pipelineId);

        kafkaSelector = new KafkaSelector(pipelineId, configService, configService.getEntityDesc(pipeline.getSourceEntityId()));
        kafkaSelector.start();
    }

    @Override
    public boolean isStart() {
        return kafkaSelector.isStart();
    }

    @Override
    public void stop() {
        kafkaSelector.stop();
    }

    @Override
    public Optional<Message<EventDataTransaction>> selector() throws InterruptedException {
        return kafkaSelector.selector();
    }

    @Override
    public void ack(Long id) {
        kafkaSelector.ack(id);
    }

    @Override
    public void rollback() {
        kafkaSelector.rollback();
    }
}
