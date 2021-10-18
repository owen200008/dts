package com.utopia.data.transfer.core.code.service.impl.task;

import com.utopia.data.transfer.core.archetype.base.ServiceException;
import com.utopia.data.transfer.core.code.base.ErrorCode;
import com.utopia.data.transfer.core.code.service.ArbitrateEventService;
import com.utopia.data.transfer.core.code.service.ConfigService;
import com.utopia.data.transfer.core.code.service.MessageParser;
import com.utopia.data.transfer.core.code.service.impl.TaskImpl;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadRun;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadTransferFacade;
import com.utopia.data.transfer.model.code.transfer.TransferData;
import com.utopia.exception.UtopiaRunTimeException;
import com.utopia.extension.UtopiaExtensionLoader;
import com.utopia.log.BasicLogUtil;
import com.utopia.model.rsp.UtopiaErrorCodeClass;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.ServiceBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import java.util.concurrent.locks.LockSupport;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
@Slf4j
public class LoadTaskImpl extends TaskImpl implements LoadTransferFacade {

    private static UtopiaResponseModel SUCCESS = UtopiaResponseModel.success();
    private static UtopiaResponseModel EXCEPTION = UtopiaResponseModel.fail(ErrorCode.LOAD_RUN_EXCEPTION);
    private static UtopiaResponseModel CLOSED = UtopiaResponseModel.fail(ErrorCode.LOAD_RUN_CLOSED);

    public static String TRANSFER_VERSION = "1.0.0";

    private final ApplicationContext applicationContext;
    private final ApplicationEventPublisher applicationEventPublisher;

    private ServiceBean<LoadTransferFacade> serviceBean;

    private LoadRun.LoadRunItem loadRun;

    // 运行调度控制
    private volatile boolean            isStart             = false;
    private volatile long               lastUpdateTime      = System.currentTimeMillis();

    public LoadTaskImpl(ConfigService configService, MessageParser messageParser, ArbitrateEventService arbitrateEventService,
                        ApplicationContext applicationContext,
                        ApplicationEventPublisher applicationEventPublisher) {
        super(configService, messageParser, arbitrateEventService);
        this.applicationContext = applicationContext;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void run() {
        log.info("start");
        try {
            while (running) {
                try {
                    if (isStart) {
                        boolean working = arbitrateEventService.getPipelineEventService().checkLoadResource(pipelineId);
                        if (!working) {
                            stopup();
                        }
                        this.lastUpdateTime = System.currentTimeMillis();
                        // 5秒钟检查一次
                        LockSupport.parkNanos(5 * 1000 * 1000L * 1000L);
                    } else {
                        if(!startup()){
                            // sleep 10秒再进行重试
                            try {
                                Thread.sleep(10 * 1000);
                            } catch (InterruptedException e1) {
                            }
                        }
                    }
                } catch (Throwable e) {
                    if (isInterrupt(e)) {
                        log.info("load is interrupt", e);
                        return;
                    } else {
                        log.warn("load is failed.", e);
                        // sleep 10秒再进行重试
                        try {
                            Thread.sleep(10 * 1000);
                        } catch (InterruptedException e1) {
                        }
                    }
                }
            }
        } finally {
            stopup();
            log.info("finish");
        }
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

    private boolean startup() {
        //获取资源
        try {
            arbitrateEventService.getPipelineEventService().waitLoadResource(pipelineId);

            LoadRun extension = UtopiaExtensionLoader.getExtensionLoader(LoadRun.class).getExtension(this.targetEntityDesc.getType().name());
            if(Objects.isNull(extension)) {
                log.error("get load extension error {}", String.valueOf(this.targetEntityDesc.getType()));
                throw new UtopiaRunTimeException(ErrorCode.LOAD_GET_EXTENSION_FAIL);
            }
            this.loadRun = extension.createItem(pipeline, sourceEntityDesc, targetEntityDesc);
            if(this.loadRun == null){
                log.error("get load extension createItem error {}", String.valueOf(this.targetEntityDesc.getType()));
                throw new UtopiaRunTimeException(ErrorCode.LOAD_GET_EXTENSION_FAIL);
            }

            //启动dubbo服务
            serviceBean = createGlobalService(applicationContext, applicationEventPublisher, LoadTransferFacade.class, TRANSFER_VERSION, String.valueOf(pipelineId));

            serviceBean.setRef(this);
            serviceBean.export();

            isStart = true;
            return true;
        } catch (Exception e) {
            log.error("startup waitResource exception {}", pipelineId, e);
            try {
                arbitrateEventService.getPipelineEventService().releaseLoadResource(pipelineId);
            } catch (ExecutionException ex) {
                log.error("releaseResource exception {}", pipelineId, ex);
            }
        }
        return false;
    }

    private void stopup() {
        if (isStart) {
            //先注销
            serviceBean.unexport();
            serviceBean = null;

            this.loadRun.close();
            this.loadRun = null;

            //默认等待3s
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            try {
                arbitrateEventService.getPipelineEventService().releaseLoadResource(pipelineId);
            } catch (ExecutionException ex) {
                log.error("releaseResource exception {}", pipelineId, ex);
            }
            isStart = false;
        }
    }

    @Override
    public CompletableFuture<UtopiaResponseModel> transfer(TransferData transferData) {
//                    //首先判断是否还允许load
////                    //开始select的时间
////                    long startTime = System.currentTimeMillis();
////                    //10s取一次一定执行完成
////                    if(lastUpdateTime + pipeline.getParams().getResourceTimeout() * 1000
////                            - pipeline.getParams().getProcessTimeout() * 1000
////                            - 1000 <= startTime) {
////                        //如果时间不够的话
////                        return CompletableFuture.completedFuture(false);
////                    }
////
////                    return null;
        Thread.currentThread().setName(createTaskName(pipelineId, "DubboWorker"));
        if(!running){
            return CompletableFuture.completedFuture(CLOSED);
        }

        try{
            UtopiaErrorCodeClass load = loadRun.load(transferData);
            if(load.getCode() != ErrorCode.CODE_SUCCESS.getCode()){
                return CompletableFuture.completedFuture(UtopiaResponseModel.fail(load));
            }
            //如果成功的话，记录下以免重复执行

        } catch(ServiceException e){
            log.error("load exception {}", e.getCode(), e);
            return CompletableFuture.completedFuture(EXCEPTION);
        }
        return CompletableFuture.completedFuture(SUCCESS);
    }
}
