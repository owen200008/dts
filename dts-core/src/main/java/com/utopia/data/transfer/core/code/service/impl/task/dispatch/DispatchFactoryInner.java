package com.utopia.data.transfer.core.code.service.impl.task.dispatch;

import com.utopia.data.transfer.core.code.model.EventDataTransaction;
import com.utopia.data.transfer.core.code.model.Message;
import com.utopia.data.transfer.core.code.service.impl.task.LoadTaskImpl;
import com.utopia.data.transfer.core.code.service.impl.task.SelectTaskImpl;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadTransferFacade;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author owen.cai
 * @create_date 2021/9/30
 * @alter_author
 * @alter_date
 */
@Slf4j
public class DispatchFactoryInner implements DispatchFactory {

    private static ConcurrentHashMap<Long, LoadTransferFacade> map = new ConcurrentHashMap();

    private static void addInnerDispatch(Long pipelineId, LoadTransferFacade dispatchInner){
        map.put(pipelineId, dispatchInner);
    }

    private static void delInnerDispatch(Long pipelineId) {
        map.remove(pipelineId);
    }

    private static LoadTransferFacade getLoadDispatchInner(Long pipelineId){
        return map.get(pipelineId);
    }

    @Override
    public SelectDispatchRule createSelectDispatchRule(String dispatchRuleParam) {
        return new SelectDispatchInner(dispatchRuleParam);
    }

    @Override
    public LoadDispatchRule createLoadDispatchRule(String dispatchRuleParam) {
        return new LoadDispatchInner();
    }

    public static class SelectDispatchInner implements SelectDispatchRule {

        public SelectDispatchInner(String dispatchRuleParam) {

        }

        @Override
        public boolean start(SelectTaskImpl selectTask) {
            return true;
        }

        @Override
        public CompletableFuture<UtopiaResponseModel> dispatch(Pipeline pipeline, Message<EventDataTransaction> message) {
            LoadTransferFacade loadDispatchInner = DispatchFactoryInner.getLoadDispatchInner(pipeline.getId());
            if(Objects.isNull(loadDispatchInner)){
                return CompletableFuture.completedFuture(DispatchFactory.FACADE_ERROR);
            }
            return loadDispatchInner.inner(message);
        }
    }

    public static class LoadDispatchInner implements LoadDispatchRule {

        private Long pipelineId;

        @Override
        public void start(LoadTaskImpl loadTask) {
            this.pipelineId = loadTask.getPipelineId();
            DispatchFactoryInner.addInnerDispatch(this.pipelineId, loadTask);
        }

        @Override
        public void stop() {
            DispatchFactoryInner.delInnerDispatch(this.pipelineId);
        }
    }
}
