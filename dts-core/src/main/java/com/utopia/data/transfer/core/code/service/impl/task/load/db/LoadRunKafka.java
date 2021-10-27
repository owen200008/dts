package com.utopia.data.transfer.core.code.service.impl.task.load.db;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.utopia.data.transfer.core.code.model.EventDataTransaction;
import com.utopia.data.transfer.core.code.model.Message;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadRun;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.model.archetype.ServiceException;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;

import com.utopia.data.transfer.model.code.transfer.TransferEventDataTransaction;
import com.utopia.extension.UtopiaExtensionLoader;
import com.utopia.extension.UtopiaSPIInject;
import com.utopia.model.rsp.UtopiaErrorCodeClass;
import com.utopia.module.template.queue.provider.api.UtopiaProviderByteArrayApi;
import com.utopia.module.template.queue.provider.api.UtopiaProviderFactoryApi;
import com.utopia.module.template.queue.provider.api.UtopiaResult;
import com.utopia.module.template.queue.provider.kafka.UtopiaProviderKafkaConf;
import com.utopia.serialization.api.SerializationApi;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author owen.cai
 * @create_date 2021/10/13
 * @alter_author
 * @alter_date
 */
@Slf4j
public class LoadRunKafka implements LoadRun {

    private static String KAFKA_BYTEARRAY_TEMPLATE = "kafkaTemplateByteArray";

    @UtopiaSPIInject
    private DefaultListableBeanFactory beanDefinitionRegistry;

    @Override
    public LoadRunItem createItem(Pipeline pipeline, EntityDesc sourceEntityDesc, EntityDesc targetEntityDesc) {
        return new LoadRunKafkaItem(pipeline, sourceEntityDesc, targetEntityDesc, beanDefinitionRegistry);
    }

    public static class LoadRunKafkaItem implements LoadRunItem{

        private final Pipeline pipeline;
        private final EntityDesc sourceEntityDesc;
        private final EntityDesc targetEntityDesc;

        private SerializationApi serializationApi;
        private UtopiaProviderByteArrayApi utopiaProviderByteArrayApi;

        public LoadRunKafkaItem(Pipeline pipeline, EntityDesc sourceEntityDesc, EntityDesc targetEntityDesc, DefaultListableBeanFactory beanDefinitionRegistry) {
            this.pipeline = pipeline;
            this.sourceEntityDesc = sourceEntityDesc;
            this.targetEntityDesc = targetEntityDesc;

            this.serializationApi = UtopiaExtensionLoader.getExtensionLoader(SerializationApi.class).getExtension(targetEntityDesc.getKafka().getSerialization());
            if(Objects.isNull(this.serializationApi)){
                log.error("no find serializationApi {}", targetEntityDesc.getKafka().getSerialization());
                throw new ServiceException(ErrorCode.LOAD_NO_SERIALIZATION);
            }
            //手动创建kafka的provider
            UtopiaProviderFactoryApi kafka = UtopiaExtensionLoader.getExtensionLoader(UtopiaProviderFactoryApi.class).getExtension("kafka");

            UtopiaProviderKafkaConf kafkaConf = new UtopiaProviderKafkaConf();
            kafkaConf.setByteArray(true);
            kafkaConf.setKafkaTemplateInjectName(KAFKA_BYTEARRAY_TEMPLATE);
            kafkaConf.setProviderTopic(targetEntityDesc.getKafka().getTopic());

            String keyName = String.format("loadRunKafkaItem_Template_%d", pipeline.getId());
            Supplier<UtopiaProviderByteArrayApi> supplier = () -> kafka.createProviderByteArray((JSONObject) JSON.toJSON(kafkaConf));
            RootBeanDefinition bean = new RootBeanDefinition(UtopiaProviderByteArrayApi.class, supplier);
            beanDefinitionRegistry.registerBeanDefinition(keyName, bean);

            utopiaProviderByteArrayApi = (UtopiaProviderByteArrayApi) beanDefinitionRegistry.getBean(keyName);
            if(Objects.isNull(utopiaProviderByteArrayApi)){
                log.error("utopiaProviderByteArrayApi topic {}", targetEntityDesc.getKafka().getTopic());
                throw new ServiceException(ErrorCode.LOAD_NO_KAFKA);
            }
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
                UtopiaResult utopiaResult = utopiaProviderByteArrayApi.put(null, this.serializationApi.writeOnce(message)).get();
                if(utopiaResult.equals(UtopiaResult.Succ)){
                    return ErrorCode.CODE_SUCCESS;
                }
                return ErrorCode.LOAD_KAFKA_PUT_FAIL;
            } catch (Throwable e) {
                log.error("kafka put error", e);
            }
            return ErrorCode.UNKNOW_ERROR;
        }

        @Override
        public void close() {
            utopiaProviderByteArrayApi.close();
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
