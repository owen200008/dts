package com.utopia.data.transfer.core.code.kafka;

import com.utopia.data.transfer.core.code.model.EventDataTransaction;
import com.utopia.data.transfer.core.code.model.Message;
import com.utopia.data.transfer.core.code.service.ConfigService;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.model.archetype.ServiceException;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.entity.kafka.KafkaProperty;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.extension.UtopiaExtensionLoader;
import com.utopia.serialization.api.SerializationApi;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.h2.util.NetUtils;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author owen.cai
 * @create_date 2021/10/26
 * @alter_author
 * @alter_date
 */
@Slf4j
public class KafkaSelector {

    @Getter
    private Pipeline                        pipeline;
    private final EntityDesc                source;
    private ConfigService                   configService;
    private final KafkaProperties           kafkaProperties;
    private SerializationApi                serializationApi;
    private KafkaConsumer<String, byte[]>   stringKafkaConsumer;
    private Duration timeoutDuration;

    /**
     * 是否处于运行中
     */
    private volatile boolean        running          = false;

    public KafkaSelector(Long pipelineId, ConfigService configService, EntityDesc source, KafkaProperties kafkaProperties) {
        this.configService = configService;
        this.pipeline = configService.getPipeline(pipelineId);
        this.kafkaProperties = kafkaProperties;
        this.source = source;
        this.timeoutDuration = Duration.ofMillis(pipeline.getParams().getBatchTimeout());
    }

    public void start() {
        if (running) {
            return;
        }
        EntityDesc entityDesc = configService.getEntityDesc(pipeline.getSourceEntityId());

        KafkaProperty kafka = entityDesc.getParams().toJavaObject(KafkaProperty.class);
        this.serializationApi = UtopiaExtensionLoader.getExtensionLoader(SerializationApi.class).getExtension(kafka.getSerialization());
        if(Objects.isNull(this.serializationApi)){
            log.error("no find serializationApi {}", kafka.getSerialization());
            throw new ServiceException(ErrorCode.SELECT_NO_SERIALIZATION);
        }

        //Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        this.stringKafkaConsumer = new KafkaConsumer(KafkaConfig.buildConsumerProperties(kafka, this.pipeline));

        stringKafkaConsumer.subscribe(Collections.singletonList(kafka.getTopic()));
        running = true;
    }

    public boolean isStart() {
        return running;
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        stringKafkaConsumer.unsubscribe();
        stringKafkaConsumer.close();
    }

    public Optional<Message<EventDataTransaction>> selector() throws InterruptedException {
        ConsumerRecords<String, byte[]> poll = stringKafkaConsumer.poll(this.timeoutDuration);
        if(poll.isEmpty()){
            //no data
            return Optional.empty();
        }
        if (!running) {
            throw new InterruptedException();
        }

        List<EventDataTransaction> entries = new ArrayList();
        Message<EventDataTransaction> result = new Message(0L, entries);

        for (ConsumerRecord<String, byte[]> stringConsumerRecord : poll) {
            Message<EventDataTransaction> message = this.serializationApi.read(stringConsumerRecord.value(), Message.class);
            result.setId(message.getId());
            entries.addAll(message.getDatas());
        }

        return Optional.of(result);
    }

    public void ack(Long id) {
        stringKafkaConsumer.commitSync();
    }

    public void rollback() {

    }
}
