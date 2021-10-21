package com.utopia.data.transfer.core.code.service.impl.task.load.db;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.utopia.data.transfer.core.code.model.EventDataTransaction;
import com.utopia.data.transfer.core.code.model.Message;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadRun;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;

import com.utopia.data.transfer.model.code.transfer.TransferEventDataTransaction;
import com.utopia.model.rsp.UtopiaErrorCodeClass;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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

        private static final GroovyShell SHELL = new GroovyShell();

        private List<String> params;
        private Closure closure;

        public LoadRunKafkaItem(Pipeline pipeline, EntityDesc sourceEntityDesc, EntityDesc targetEntityDesc) {

        }

        public boolean init(String dispatchRuleParam) {
            EvaluateClosureParam evaluateClosureParam = JSON.parseObject(dispatchRuleParam, EvaluateClosureParam.class);
            this.closure = evaluateClosure(evaluateClosureParam.getQuery());
            this.closure.setResolveStrategy(Closure.DELEGATE_ONLY);
            this.params = evaluateClosureParam.getParams();
            return true;
        }

        @Override
        public UtopiaErrorCodeClass load(Message<TransferEventDataTransaction> transferData) {
            //目前不支持传输模式
            return ErrorCode.UNKNOW_ERROR;
        }

        @Override
        public UtopiaErrorCodeClass loadInner(Message<EventDataTransaction> message) {
            //所有的数据序列化打入kafka
            for (EventDataTransaction data : message.getDatas()) {
//            if(CollectionUtils.isEmpty(this.params)){
//                for (EventColumn key : data.getKeys()) {
//                    closure.setProperty(key.getColumnName(), key.getColumnValue());
//                }
//            }
//            else{
//                for (String param : this.params) {
//                    closure.setProperty(param, data.getAllColumns().get(param).getColumnValue());
//                }
//            }
//            log.info(closure.call().toString());
            }

            return null;
        }

        public Closure<?> evaluateClosure(String inlineExpression) {
            return (Closure)this.evaluate(Joiner.on("").join("{it -> \"", inlineExpression, new Object[]{"\"}"}));
        }

        private Object evaluate(final String expression) {
            Script script = SHELL.parse(expression);
            return script.run();
        }

        @Override
        public void close() {

        }
    }
}
