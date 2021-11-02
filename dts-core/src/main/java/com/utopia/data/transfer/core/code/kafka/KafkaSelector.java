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
import com.utopia.model.rsp.UtopiaErrorCodeClass;
import com.utopia.serialization.api.SerializationApi;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author owen.cai
 * @create_date 2021/10/26
 * @alter_author
 * @alter_date
 */
@Slf4j
public class KafkaSelector extends AbstractSelfCirculation<KafkaOrder> {

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
    private Thread processThread;

    public KafkaSelector(Long pipelineId, ConfigService configService, EntityDesc source, KafkaProperties kafkaProperties) {
        this.configService = configService;
        this.pipeline = configService.getPipeline(pipelineId);
        this.kafkaProperties = kafkaProperties;
        this.source = source;
        this.timeoutDuration = Duration.ofMillis(pipeline.getParams().getBatchTimeout());

        init(new SelfCirculationCallback() {
            @Override
            public boolean isRunning() {
                return running;
            }

            @Override
            public void sign() {
                //kafka不用做什么
            }
        });
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

        /**
         * 自建线程
         */
        this.processThread = new Thread(() -> {
            //自动执行
            autoRun();
        });

        this.processThread.start();
    }

    public boolean isStart() {
        return running;
    }

    public void stop() {
        if (!running) {
            return;
        }

        var kafkaOrderOptionalSelfCirculationOrder = new SelfCirculationOrder<KafkaOrder, Optional<Message<EventDataTransaction>>>(KafkaOrder.STOP);
        try{
            runAction(kafkaOrderOptionalSelfCirculationOrder);
        }catch (Throwable e){
            log.error("runAction stop error", e);
        }
        running = false;
        closeAction();

        //等待退出，最多等待10s
        try {
            processThread.join(10000);
        } catch (InterruptedException e) {
            log.error("processThread no stopup interrupt");
            processThread.interrupt();
        }
    }

    public Optional<Message<EventDataTransaction>> selector() throws InterruptedException {
        var kafkaOrderOptionalSelfCirculationOrder = new SelfCirculationOrder<KafkaOrder, Optional<Message<EventDataTransaction>>>(KafkaOrder.SELECT);
        runAction(kafkaOrderOptionalSelfCirculationOrder);
        if (!running) {
            throw new InterruptedException();
        }
        if(kafkaOrderOptionalSelfCirculationOrder.getCode().getCode() != ErrorCode.CODE_SUCCESS.getCode()) {
            log.error("error return code {}", kafkaOrderOptionalSelfCirculationOrder.getCode().getCode());
            throw new InterruptedException();
        }
        return kafkaOrderOptionalSelfCirculationOrder.getResult();
    }

    public void ack(Long id) {
        stringKafkaConsumer.commitSync();
    }

    public void rollback() {

    }

    @Override
    protected CompletableFuture<UtopiaErrorCodeClass> operator(KafkaOrder kafkaOrder, SelfCirculationOperator selfCirculationOperator) {
        switch(kafkaOrder){
            case SELECT:{
                ConsumerRecords<String, byte[]> poll = stringKafkaConsumer.poll(this.timeoutDuration);

                if(poll.isEmpty()){
                    selfCirculationOperator.setResult(Optional.empty());
                    return CompletableFuture.completedFuture(ErrorCode.CODE_SUCCESS);
                }
                List<EventDataTransaction> entries = new ArrayList();
                Message<EventDataTransaction> result = new Message(0L, entries);

                for (ConsumerRecord<String, byte[]> stringConsumerRecord : poll) {
                    Message<EventDataTransaction> message = this.serializationApi.read(stringConsumerRecord.value(), Message.class);
                    result.setId(message.getId());
                    entries.addAll(message.getDatas());
                }

                selfCirculationOperator.setResult(Optional.of(result));
                return CompletableFuture.completedFuture(ErrorCode.CODE_SUCCESS);
            }
            case STOP:{
                stringKafkaConsumer.unsubscribe();
                stringKafkaConsumer.close();
                return CompletableFuture.completedFuture(ErrorCode.CODE_SUCCESS);
            }
        }
        return null;
    }

    @Override
    protected void idle() {
        //do nothing
    }
}
