package com.utopia.data.transfer.core.extension.dispatch;

import com.utopia.data.transfer.core.base.config.KernelConfigDubbo;
import com.utopia.data.transfer.core.extension.base.dispatch.DispatchFactory;
import com.utopia.data.transfer.core.extension.base.dispatch.LoadDispatchRule;
import com.utopia.data.transfer.core.extension.base.dispatch.LoadTransferFacade;
import com.utopia.data.transfer.core.extension.base.dispatch.SelectDispatchRule;
import com.utopia.data.transfer.model.code.entity.Task;
import com.utopia.data.transfer.model.code.entity.data.EventDataTransaction;
import com.utopia.data.transfer.model.code.entity.data.Message;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.data.transfer.model.code.transfer.TransferEventData;
import com.utopia.data.transfer.model.code.transfer.TransferEventDataTransaction;
import com.utopia.extension.UtopiaSPIInject;
import com.utopia.log.BasicLogUtil;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.ClusterRules;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author owen.cai
 * @create_date 2021/9/30
 * @alter_author
 * @alter_date
 */
@Slf4j
public class DispatchFactoryDubbo implements DispatchFactory {

    public static String TRANSFER_VERSION = "1.0.0";

    @UtopiaSPIInject
    private ApplicationContext applicationContext;

    @UtopiaSPIInject
    private ApplicationEventPublisher applicationEventPublisher;

    @UtopiaSPIInject
    private KernelConfigDubbo kernelConfigDubbo;

    @Override
    public SelectDispatchRule createSelectDispatchRule(String dispatchRuleParam) {
        return new SelectDispatchDubbo(dispatchRuleParam);
    }

    @Override
    public LoadDispatchRule createLoadDispatchRule(String dispatchRuleParam) {
        return new LoadDispatchDubbo(applicationContext, applicationEventPublisher, kernelConfigDubbo);
    }

    public static class SelectDispatchDubbo implements SelectDispatchRule {

        private Boolean transferFullData;
        private LoadTransferFacade loadTransferFacade;

        public SelectDispatchDubbo(String dispatchRuleParam) {

        }

        @Override
        public boolean start(Task selectTask) {
            this.transferFullData = selectTask.getPipeline().getParams().getDispatchParamter().getTransferFullData();
            if(Objects.isNull(this.transferFullData)){
                this.transferFullData = false;
            }

            ReferenceConfig<LoadTransferFacade> reference = new ReferenceConfig();
            reference.setInterface(LoadTransferFacade.class);
            reference.setVersion(TRANSFER_VERSION);
            reference.setGroup(String.valueOf(selectTask.getPipeline().getId()));
            reference.setCluster(ClusterRules.FAIL_FAST);
            reference.setCheck(false);
            try{
                this.loadTransferFacade = reference.get();
            }catch (Throwable e) {
                log.error("pipelineid {} get facade error!!! {}", selectTask.getPipeline().getId(), e);
                return false;
            }
            return true;
        }

        @Override
        public CompletableFuture<UtopiaResponseModel> dispatch(Pipeline pipeline, Message<EventDataTransaction> message) {
            //根据pipeline获取服务
            try{
                if(this.transferFullData){
                    return loadTransferFacade.inner(message);
                }else{
                    return loadTransferFacade.transfer(new Message(message.getId(), transferTransfer(message.getDatas())));
                }
            }catch(Throwable e){
                log.error("dispatch error", e);
            }
            return CompletableFuture.completedFuture(EXCEPTION_ERROR);
        }

        private List<TransferEventDataTransaction> transferTransfer(List<EventDataTransaction> datas) {
            return datas.stream().map(data->{
                TransferEventDataTransaction transferEventData = new TransferEventDataTransaction();
                transferEventData.setGtid(data.getGtid());
                transferEventData.setDatas(data.getDatas().stream().map(item->{
                    TransferEventData tmp = new TransferEventData();
                    tmp.setTableId(item.getTableId());
                    tmp.setEventType(item.getEventType());
                    tmp.setExecuteTime(item.getExecuteTime());

                    tmp.setOldKeys(item.getOldKeys());
                    tmp.setKeys(item.getKeys());
                    tmp.setColumns(item.getColumns());

                    tmp.setSql(item.getSql());
                    tmp.setDdlSchemaName(item.getDdlSchemaName());
                    return tmp;
                }).collect(Collectors.toList()));
                return transferEventData;
            }).collect(Collectors.toList());
        }
    }

    public static class LoadDispatchDubbo implements LoadDispatchRule {

        private final ApplicationContext applicationContext;
        private final ApplicationEventPublisher applicationEventPublisher;
        private final KernelConfigDubbo kernelConfigDubbo;
        private ServiceBean<LoadTransferFacade> serviceBean;

        public LoadDispatchDubbo(ApplicationContext applicationContext, ApplicationEventPublisher applicationEventPublisher, KernelConfigDubbo kernelConfigDubbo) {
            this.applicationContext = applicationContext;
            this.applicationEventPublisher = applicationEventPublisher;
            this.kernelConfigDubbo = kernelConfigDubbo;
        }

        @Override
        public void start(Task loadTask, LoadTransferFacade loadTransferFacade) {
            //启动dubbo服务
            serviceBean = createGlobalService(applicationContext, applicationEventPublisher, LoadTransferFacade.class,
                    TRANSFER_VERSION, String.valueOf(loadTask.getPipeline().getId()), kernelConfigDubbo.getCenter());
            serviceBean.setRef(loadTransferFacade);
            serviceBean.export();
        }

        @Override
        public void stop() {
            serviceBean.unexport();
            serviceBean = null;
        }

        public static <T> ServiceBean<T> createGlobalService(ApplicationContext applicationContext,
                                                             ApplicationEventPublisher applicationEventPublisher,
                                                             Class<T> create,
                                                             String version,
                                                             String group,
                                                             String registry){
            ServiceBean<T> tmpService = new ServiceBean();
            // 弱类型接口名
            tmpService.setApplicationEventPublisher(applicationEventPublisher);
            tmpService.setApplicationContext(applicationContext);
            tmpService.setRegistryIds(registry);
            tmpService.setInterface(create);
            tmpService.setBeanName("dts");
            tmpService.setVersion(version);
            tmpService.setGroup(group);
            try {
                tmpService.afterPropertiesSet();
                return tmpService;
            } catch (Exception e) {
                BasicLogUtil.errorFormat(log, "dubbo dts afterPropertiesSet error", e);
            }
            return null;
        }
    }
}
