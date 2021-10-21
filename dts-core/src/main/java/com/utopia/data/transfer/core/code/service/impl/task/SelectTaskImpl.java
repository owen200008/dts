package com.utopia.data.transfer.core.code.service.impl.task;

import com.alibaba.fastjson.JSON;
import com.utopia.data.transfer.core.code.service.impl.task.select.SelectDataFactory;
import com.utopia.data.transfer.core.code.service.impl.task.select.SelectDataRule;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.core.code.model.EventDataTransaction;
import com.utopia.data.transfer.core.code.model.Message;
import com.utopia.data.transfer.core.code.service.ArbitrateEventService;
import com.utopia.data.transfer.core.code.service.ConfigService;
import com.utopia.data.transfer.core.code.service.impl.TaskImpl;
import com.utopia.data.transfer.core.code.service.impl.task.select.SelectDispatchRule;
import com.utopia.data.transfer.model.archetype.ServiceException;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.data.transfer.model.code.pipeline.DispatchParamter;
import com.utopia.exception.UtopiaRunTimeException;
import com.utopia.extension.UtopiaExtensionLoader;
import com.utopia.model.rsp.UtopiaResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
@Slf4j
public class SelectTaskImpl extends TaskImpl {

    /**
     * 运行调度控制
     */
    private volatile boolean            isStart             = false;
    private volatile long               lastUpdateTime      = System.currentTimeMillis();

    private SelectDataRule selectDataRule;

    private SelectDispatchRule selectDispatchRule;

    private SelectTaskPrometheus selectTaskPrometheus;

    protected Thread processThread;

    public SelectTaskImpl(ConfigService configService, ArbitrateEventService arbitrateEventService) {
        super(configService, arbitrateEventService);
    }

    @Override
    public void startTask(Long pipelineId) {
        selectTaskPrometheus = new SelectTaskPrometheus(pipelineId);
        Pipeline pipeline = configService.getPipeline(pipelineId);

        DispatchParamter selectParamter = pipeline.getParams().getDispatchParamter();
        //
        this.selectDispatchRule = UtopiaExtensionLoader.getExtensionLoader(SelectDispatchRule.class)
                .getExtension(selectParamter.getDispatchRule());
        if(this.selectDispatchRule == null) {
            log.error("no SelectDispatchRule find {}", selectParamter.getDispatchRule());
            throw new UtopiaRunTimeException(ErrorCode.SELECT_RULE_NO_FIND);
        }
        if(!this.selectDispatchRule.init(selectParamter.getDispatchRuleParam())){
            log.error("SelectDispatchRule init fail {} {}", selectParamter.getDispatchRule(), selectParamter.getDispatchRuleParam());
            throw new UtopiaRunTimeException(ErrorCode.SELECT_RULE_INIT_FAIL);
        }
        super.startTask(pipelineId);
    }

    @Override
    public void run() {
        log.info("start");
        try {
            while (running) {
                try {
                    if (isStart) {
                        boolean working = arbitrateEventService.getPipelineEventService().checkSelectResource(pipelineId);
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
                        log.info("select is interrupt", e);
                        return;
                    } else {
                        log.warn("select is failed.", e);
                        sendRollbackTermin();


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
            arbitrateEventService.getPipelineEventService().waitSelectResource(pipelineId);

            // 启动selector
            // 获取对应的selector
            SelectDataFactory extension = UtopiaExtensionLoader.getExtensionLoader(SelectDataFactory.class).getExtension(sourceEntityDesc.getType().name());
            if(Objects.isNull(extension)){
                log.error("create selectDataRule error");
                throw new ServiceException(ErrorCode.SELECT_RULE_NO_SELECT_DATA);
            }
            this.selectDataRule = extension.createSelectDataRule(pipelineId);



            processThread = new Thread(() -> {
                String currentName = Thread.currentThread().getName();
                Thread.currentThread().setName(createTaskName(pipelineId, "ProcessSelect"));
                try {
                    processSelect();
                } finally {
                    Thread.currentThread().setName(currentName);
                }
            });
            processThread.start();

            isStart = true;
            return true;
        } catch (Exception e) {
            log.error("startup waitResource exception {}", pipelineId, e);
            try {
                arbitrateEventService.getPipelineEventService().releaseSelectResource(pipelineId);
            } catch (ExecutionException ex) {
                log.error("releaseResource exception {}", pipelineId, ex);
            }
        }
        return false;
    }

    private synchronized void stopup() {
        if (isStart) {
            //等待退出，最多等待10s
            try {
                processThread.join(10000);
            } catch (InterruptedException e) {
                log.error("processThread no stopup interrupt");
                processThread.interrupt();
            }

            if (selectDataRule != null && selectDataRule.isStart()) {
                selectDataRule.stop();
            }

            try {
                arbitrateEventService.getPipelineEventService().releaseSelectResource(pipelineId);
            } catch (ExecutionException e) {
                log.error("releaseSelectResource error");
            }
            isStart = false;
        }
    }

    private void processSelect() {
        while (running) {
            try {
                //获取数据
                Optional<Message<EventDataTransaction>> selector = selectDataRule.selector();
                if(!selector.isPresent()){
                    continue;
                }

                //获取到数据
                selectTaskPrometheus.getSelectTimes().increment();

                Message<EventDataTransaction> message = selector.get();
                boolean existDatas = !CollectionUtils.isEmpty(message.getDatas());
                if(existDatas){
                    selectTaskPrometheus.getSelectTimes().increment();
                }
                do{
                    selectTaskPrometheus.getCallCounter().increment();

                    //开始select的时间
                    long startTime = System.currentTimeMillis();
                    //10s取一次一定执行完成
                    //多减少1s
                    if(this.lastUpdateTime + pipeline.getParams().getResourceTimeout() * 1000
                            - pipeline.getParams().getProcessTimeout() * 1000
                            - 1000 <= startTime){
                        selectTaskPrometheus.getTimerException().increment();
                        //等待1s后check
                        Thread.sleep(1000);
                        continue;
                    }

                    // 如果数据为空
                    if (existDatas) {
                        //这边是串行的核心是提高并发，直接按规则打入队列并发处理
                        CompletableFuture<UtopiaResponseModel> completableFuture = this.selectDispatchRule.dispatch(pipeline, message);

                        UtopiaResponseModel utopiaResponseModel = completableFuture.get(10, TimeUnit.SECONDS);
                        if(utopiaResponseModel == null || utopiaResponseModel.getCode() != ErrorCode.CODE_SUCCESS.getCode()) {

                            selectTaskPrometheus.getDispathException().increment();
                            //如果是分发失败的话，可以重复分发
                            log.error("dispatch no complate {} {}", message.getId(), JSON.toJSONString(utopiaResponseModel));
                            //睡1s
                            Thread.sleep(1000);
                            continue;
                        }
                    }
                    long endTime = System.currentTimeMillis();
                    //执行时间超过一次的限制
                    if(endTime - startTime >= pipeline.getParams().getProcessTimeout() * 1000) {
                        selectTaskPrometheus.getRunTimeException().increment();
                        log.warn("Select Process Timeout {}", endTime - startTime);
                        if(checkResourceTimeout(endTime, message.getId())) {
                            //rollback重新获取
                            break;
                        }
                    }
                    //确认
                    selectDataRule.ack(message.getId());
                    break;
                }while(running);
            } catch (Throwable e) {
                if (!isInterrupt(e)) {
                    log.error(String.format("[%s] selectTask is error!", pipelineId), e);
                    sendRollbackTermin();
                } else {
                    log.info(String.format("[%s] selectTask is interrrupt!", pipelineId), e);
                    return;
                }
            }
        }
    }

    protected void sendRollbackTermin() {
        selectTaskPrometheus.getRollbackException().increment();
        selectDataRule.rollback();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected boolean checkResourceTimeout(long endTime, Long batchId){
        //判断锁还是不是自己的，如果是的话可以ack
        //多减少1s
        if(endTime >= this.lastUpdateTime + pipeline.getParams().getResourceTimeout() * 1000 - 1000){
            //时间过了
            log.error(String.format("checkResourceTimeout Timeout batchId {}", batchId));
            sendRollbackTermin();
            return true;
        }
        return false;
    }
}
