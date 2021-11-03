package com.utopia.data.transfer.core.code.service.impl.task.load.db;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.utopia.data.transfer.core.code.kafka.KafkaConfig;
import com.utopia.data.transfer.core.code.model.EventDataTransaction;
import com.utopia.data.transfer.core.code.model.Message;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadRun;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.model.archetype.ServiceException;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.entity.kafka.KafkaProperty;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;

import com.utopia.data.transfer.model.code.transfer.TransferEventDataTransaction;
import com.utopia.extension.UtopiaExtensionLoader;
import com.utopia.extension.UtopiaSPIInject;
import com.utopia.model.rsp.UtopiaErrorCodeClass;
import com.utopia.serialization.api.SerializationApi;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author owen.cai
 * @create_date 2021/10/13
 * @alter_author
 * @alter_date
 */
@Slf4j
public class LoadRunKafka implements LoadRun {

    @Override
    public LoadRunItem createItem(Pipeline pipeline, EntityDesc sourceEntityDesc, EntityDesc targetEntityDesc) {
        return new LoadRunKafkaItem(pipeline, sourceEntityDesc, targetEntityDesc);
    }

    public static class LoadRunKafkaItem implements LoadRunItem{

        private final Pipeline pipeline;
        private final EntityDesc sourceEntityDesc;
        private final EntityDesc targetEntityDesc;

        private String topic;
        private SerializationApi serializationApi;
        private KafkaProducer<String, byte[]> producer;

        public LoadRunKafkaItem(Pipeline pipeline, EntityDesc sourceEntityDesc, EntityDesc targetEntityDesc) {
            this.pipeline = pipeline;
            this.sourceEntityDesc = sourceEntityDesc;
            this.targetEntityDesc = targetEntityDesc;

            KafkaProperty kafkaParam = targetEntityDesc.getParams().toJavaObject(KafkaProperty.class);
            this.serializationApi = UtopiaExtensionLoader.getExtensionLoader(SerializationApi.class).getExtension(kafkaParam.getSerialization());
            if(Objects.isNull(this.serializationApi)){
                log.error("no find serializationApi {}", kafkaParam.getSerialization());
                throw new ServiceException(ErrorCode.LOAD_NO_SERIALIZATION);
            }

            this.topic = kafkaParam.getTopic();
            //Map<String, Object> props = this.kafkaProperties.buildProducerProperties();
            this.producer = new KafkaProducer(KafkaConfig.buildProducerProperties(kafkaParam, pipeline));
        }

        @Override
        public UtopiaErrorCodeClass load(Message<TransferEventDataTransaction> transferData) {
            //目前不支持传输模式
            return ErrorCode.UNKNOW_ERROR;
        }

        @Override
        public UtopiaErrorCodeClass loadInner(Message<EventDataTransaction> message) {
            //目前只支持单topic，单分区
            try{
                ProducerRecord<String, byte[]> record = new ProducerRecord(this.topic, this.serializationApi.writeOnce(message));
                //最多5s
                this.producer.send(record).get(5000, TimeUnit.SECONDS);
                return ErrorCode.CODE_SUCCESS;
            } catch (Throwable e) {
                log.error("kafka put error", e);
            }
            return ErrorCode.UNKNOW_ERROR;
        }

        @Override
        public void close() {
            producer.close();
        }

        private static final GroovyShell SHELL = new GroovyShell();

        private List<String> params;
        private Closure closure;

        public boolean init(String dispatchRuleParam) {
            EvaluateClosureParam evaluateClosureParam = JSON.parseObject(dispatchRuleParam, EvaluateClosureParam.class);
            this.closure = evaluateClosure(evaluateClosureParam.getQuery());
            this.closure.setResolveStrategy(Closure.DELEGATE_ONLY);
            this.params = evaluateClosureParam.getParams();
            return true;
        }

        public Closure<?> evaluateClosure(String inlineExpression) {
            return (Closure)this.evaluate(Joiner.on("").join("{it -> \"", inlineExpression, new Object[]{"\"}"}));
        }

        private Object evaluate(final String expression) {
            Script script = SHELL.parse(expression);
            return script.run();
        }
    }
}
