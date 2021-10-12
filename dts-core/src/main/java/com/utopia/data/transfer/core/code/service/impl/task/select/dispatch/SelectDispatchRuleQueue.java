package com.utopia.data.transfer.core.code.service.impl.task.select.dispatch;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.utopia.data.transfer.core.code.model.EventData;
import com.utopia.data.transfer.core.code.model.Message;
import com.utopia.data.transfer.core.code.service.impl.task.select.SelectDispatchRule;
import com.utopia.data.transfer.core.code.src.model.EventColumn;
import com.utopia.utils.BooleanMutex;
import com.utopia.utils.CollectionUtils;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/9/30
 * @alter_author
 * @alter_date
 */
@Slf4j
public class SelectDispatchRuleQueue implements SelectDispatchRule {

    //同步单线程使用成员应该OK
    private BooleanMutex booleanMutex = new BooleanMutex();

    private static final GroovyShell SHELL = new GroovyShell();

    private List<String> params;
    private Closure closure;

    @Override
    public boolean init(String dispatchRuleParam) {
        EvaluateClosureParam evaluateClosureParam = JSON.parseObject(dispatchRuleParam, EvaluateClosureParam.class);
        this.closure = evaluateClosure(evaluateClosureParam.getQuery());
        this.closure.setResolveStrategy(Closure.DELEGATE_ONLY);
        this.params = evaluateClosureParam.getParams();
        return true;
    }

    @Override
    public BooleanMutex dispatch(Message<EventData> message) {
        //初始化为false
        booleanMutex.set(false);

        for (EventData data : message.getDatas()) {
            if(CollectionUtils.isEmpty(this.params)){
                for (EventColumn key : data.getKeys()) {
                    closure.setProperty(key.getColumnName(), key.getColumnValue());
                }
            }
            else{
                for (String param : this.params) {
                    closure.setProperty(param, data.getAllColumns().get(param).getColumnValue());
                }
            }
            log.info(closure.call().toString());
        }
        booleanMutex.set(true);
        return booleanMutex;
    }

    public Closure<?> evaluateClosure(String inlineExpression) {
        return (Closure)this.evaluate(Joiner.on("").join("{it -> \"", inlineExpression, new Object[]{"\"}"}));
    }

    private Object evaluate(final String expression) {
        Script script = SHELL.parse(expression);
        return script.run();
    }
}
