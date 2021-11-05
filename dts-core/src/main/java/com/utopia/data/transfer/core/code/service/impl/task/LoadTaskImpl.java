package com.utopia.data.transfer.core.code.service.impl.task;

import com.utopia.data.transfer.model.code.entity.data.EventDataTransaction;
import com.utopia.data.transfer.model.code.entity.data.Message;
import com.utopia.data.transfer.core.code.service.impl.task.dispatch.DispatchFactory;
import com.utopia.data.transfer.core.code.service.impl.task.dispatch.LoadDispatchRule;
import com.utopia.data.transfer.model.archetype.ServiceException;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.core.code.service.ArbitrateEventService;
import com.utopia.data.transfer.core.code.service.ConfigService;
import com.utopia.data.transfer.core.code.service.impl.TaskImpl;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadRun;
import com.utopia.data.transfer.core.code.service.impl.task.load.LoadTransferFacade;
import com.utopia.data.transfer.model.code.pipeline.DispatchParamter;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.data.transfer.model.code.transfer.TransferEventDataTransaction;
import com.utopia.exception.UtopiaRunTimeException;
import com.utopia.extension.UtopiaExtensionLoader;
import com.utopia.model.rsp.UtopiaErrorCodeClass;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;

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

    private LoadRun.LoadRunItem loadRun;

    private LoadDispatchRule loadDispatchRule;

    private LoadTaskPrometheus loadTaskPrometheus;

    private volatile boolean            isStart             = false;
    private volatile long               lastUpdateTime      = System.currentTimeMillis();

    public LoadTaskImpl(ConfigService configService, ArbitrateEventService arbitrateEventService) {
        super(configService, arbitrateEventService);
    }

    @Override
    public boolean startTask(Long pipelineId) {
        Pipeline pipeline = configService.getPipeline(pipelineId);

        loadTaskPrometheus = new LoadTaskPrometheus(pipelineId);

        DispatchParamter selectParamter = pipeline.getParams().getDispatchParamter();

        DispatchFactory extension = UtopiaExtensionLoader.getExtensionLoader(DispatchFactory.class)
                .getExtension(selectParamter.getDispatchRule());
        if(Objects.isNull(extension)) {
            log.error("no loadDispatchRule find {}", selectParamter.getDispatchRule());
            throw new UtopiaRunTimeException(ErrorCode.LOAD_RULE_NO_FIND);
        }
        this.loadDispatchRule = extension.createLoadDispatchRule(selectParamter.getDispatchLoadParam());
        if(this.loadDispatchRule == null) {
            log.error("LoadDispatchRule init fail {} {}", selectParamter.getDispatchRule(), selectParamter.getDispatchLoadParam());
            throw new UtopiaRunTimeException(ErrorCode.LOAD_RULE_CREATE_FAIL);
        }
        return super.startTask(pipelineId);
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

    private boolean startup() {
        //获取资源
        try {
            arbitrateEventService.getPipelineEventService().waitLoadResource(pipelineId);

            LoadRun extension = UtopiaExtensionLoader.getExtensionLoader(LoadRun.class).getExtension(this.targetEntityDesc.getType().name());
            if(Objects.isNull(extension)) {
                log.error("get load extension error {}", this.targetEntityDesc.getType());
                throw new UtopiaRunTimeException(ErrorCode.LOAD_GET_EXTENSION_FAIL);
            }
            this.loadRun = extension.createItem(pipeline, sourceEntityDesc, targetEntityDesc);
            if(this.loadRun == null){
                log.error("get load extension createItem error {}", this.targetEntityDesc.getType());
                throw new UtopiaRunTimeException(ErrorCode.LOAD_GET_EXTENSION_FAIL);
            }

            loadDispatchRule.start(this);

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
            loadDispatchRule.stop();

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
    public CompletableFuture<UtopiaResponseModel> transfer(Message<TransferEventDataTransaction> transferData) {
        Thread.currentThread().setName(createTaskName(pipelineId, "TransferWorker"));
        if(!running){
            return CompletableFuture.completedFuture(CLOSED);
        }

        try{
            UtopiaErrorCodeClass load = loadRun.load(transferData);
            if(load.getCode() != ErrorCode.CODE_SUCCESS.getCode()){
                loadTaskPrometheus.getLoadError().increment();
                return CompletableFuture.completedFuture(UtopiaResponseModel.fail(load));
            }
        } catch(ServiceException e){
            loadTaskPrometheus.getLoadException().increment();
            log.error("load exception {}", e.getCode(), e);
            return CompletableFuture.completedFuture(EXCEPTION);
        }
        return CompletableFuture.completedFuture(SUCCESS);
    }

    @Override
    public CompletableFuture<UtopiaResponseModel> inner(Message<EventDataTransaction> message) {
        Thread.currentThread().setName(createTaskName(pipelineId, "InnerWorker"));
        if(!running){
            return CompletableFuture.completedFuture(CLOSED);
        }

        try{
            UtopiaErrorCodeClass load = loadRun.loadInner(message);
            if(load.getCode() != ErrorCode.CODE_SUCCESS.getCode()){
                loadTaskPrometheus.getLoadError().increment();
                return CompletableFuture.completedFuture(UtopiaResponseModel.fail(load));
            }
        } catch(ServiceException e){
            loadTaskPrometheus.getLoadException().increment();
            log.error("load exception {}", e.getCode(), e);
            return CompletableFuture.completedFuture(EXCEPTION);
        }
        return CompletableFuture.completedFuture(SUCCESS);
    }
}
