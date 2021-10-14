package com.utopia.data.transfer.core.code.service.impl.task.select.dispatch;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.utopia.data.transfer.core.code.model.EventData;
import com.utopia.data.transfer.core.code.model.Message;
import com.utopia.data.transfer.core.code.service.impl.task.select.SelectDispatchRule;
import com.utopia.data.transfer.model.code.entity.EventColumn;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.model.rsp.UtopiaResponseModel;
import com.utopia.service.mgr.model.cons.ErrorCode;
import com.utopia.utils.CollectionUtils;
import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author owen.cai
 * @create_date 2021/9/30
 * @alter_author
 * @alter_date
 */
@Slf4j
public class SelectDispatchRuleQueue implements SelectDispatchRule {

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
    public CompletableFuture<UtopiaResponseModel> dispatch(Pipeline pipeline, Message<EventData> message) {
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
        return CompletableFuture.completedFuture(UtopiaResponseModel.create(Math.random() > 0.5 ? ErrorCode.CODE_SUCCESS.getCode() : ErrorCode.UNKNOW_ERROR.getCode(),null, null));
    }

    public Closure<?> evaluateClosure(String inlineExpression) {
        return (Closure)this.evaluate(Joiner.on("").join("{it -> \"", inlineExpression, new Object[]{"\"}"}));
    }

    private Object evaluate(final String expression) {
        Script script = SHELL.parse(expression);
        return script.run();
    }
}
