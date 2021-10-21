package com.utopia.data.transfer.core.code.service.impl.task.load.dispatch;

import com.utopia.data.transfer.core.code.service.impl.task.LoadTaskImpl;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadDispatchFactory;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadDispatchRule;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadTransferFacade;
import com.utopia.extension.UtopiaSPIInject;
import com.utopia.log.BasicLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

/**
 * @author owen.cai
 * @create_date 2021/10/21
 * @alter_author
 * @alter_date
 */
@Slf4j
public class LoadDispatchFactoryDubbo implements LoadDispatchFactory {

    public static String TRANSFER_VERSION = "1.0.0";

    @UtopiaSPIInject
    private ApplicationContext applicationContext;

    @UtopiaSPIInject
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public LoadDispatchRule create(String dispatchRuleParam) {
        return new DispathDubbo(applicationContext, applicationEventPublisher);
    }

    public static class DispathDubbo implements LoadDispatchRule {

        private final ApplicationContext applicationContext;
        private final ApplicationEventPublisher applicationEventPublisher;
        private ServiceBean<LoadTransferFacade> serviceBean;

        public DispathDubbo(ApplicationContext applicationContext, ApplicationEventPublisher applicationEventPublisher) {
            this.applicationContext = applicationContext;
            this.applicationEventPublisher = applicationEventPublisher;
        }

        @Override
        public void start(LoadTaskImpl loadTask) {
            //启动dubbo服务
            serviceBean = createGlobalService(applicationContext, applicationEventPublisher, LoadTransferFacade.class, TRANSFER_VERSION, String.valueOf(loadTask.getPipelineId()));
            serviceBean.setRef(loadTask);
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
                                                             String group){
            ServiceBean<T> tmpService = new ServiceBean();
            // 弱类型接口名
            tmpService.setApplicationEventPublisher(applicationEventPublisher);
            tmpService.setApplicationContext(applicationContext);
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
