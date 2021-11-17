package com.utopia.data.transfer.core.extension.select.kafka;

import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.exception.UtopiaRunTimeException;
import com.utopia.model.rsp.UtopiaErrorCodeClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author owen.cai
 * @create_date 2021/11/2
 * @alter_author
 * @alter_date
 */
@Slf4j
public abstract class AbstractSelfCirculation<Type> {

    private BlockingQueue<SelfCirculationOrder<Type, ?>> queue = new LinkedBlockingQueue();

    private SelfCirculationCallback selfCirculationThread;

    public void init(SelfCirculationCallback selfCirculationThread) {
        this.selfCirculationThread = selfCirculationThread;
    }

    protected void runAction(SelfCirculationOrder<Type, ?> selfCirculationOrder) throws UtopiaRunTimeException, InterruptedException {
        //加入队列
        queue.add(selfCirculationOrder);
        selfCirculationThread.sign();
        selfCirculationOrder.getBooleanMutex().get();
    }

    public void closeAction(){
        queue.add(SelfCirculationOrder.createClose());
    }

    public void autoRun() {
        while(selfCirculationThread.isRunning()) {
            try{
                Operator(queue.take());
            } catch(Throwable e) {
                log.error("autoRun take throwable", e);
                try {
                    //默认异常1s后执行
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    log.error("sleep error", ex);
                }
            }
        }
        //退出前，队列所有的请求全部处理
        for (SelfCirculationOrder<Type, ?> typeSelfCirculationOrder : queue) {
            typeSelfCirculationOrder.finish(ErrorCode.ERROR_EXIT);
            typeSelfCirculationOrder.getBooleanMutex().set(true);
        }
    }

    public void autoRunWithTimeout(long timeout, TimeUnit unit) throws InterruptedException {
        while(selfCirculationThread.isRunning()) {
            try{
                Operator(queue.poll(timeout, unit));
            } catch(Throwable e) {
                log.error("autoRun take throwable", e);
                try {
                    //默认异常1s后执行
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    log.error("sleep error", ex);
                }
            }
        }
        //退出前，队列所有的请求全部处理
        for (SelfCirculationOrder<Type, ?> typeSelfCirculationOrder : queue) {
            typeSelfCirculationOrder.finish(ErrorCode.ERROR_EXIT);
            typeSelfCirculationOrder.getBooleanMutex().set(true);
        }
    }

    protected void Operator(SelfCirculationOrder<Type, ?> take) {
        if(Objects.isNull(take)) {
            idle();
            return;
        }
        if(take.isClose()){
            return;
        }
        try{
            CompletableFuture<UtopiaErrorCodeClass> operator = operator(take.getType(), take);
            operator.whenComplete((r, ex)->{
                if(Objects.nonNull(ex)) {
                    log.error("operator callback ", ex);
                    take.finish(ErrorCode.UNKNOW_ERROR);
                    return;
                }
                take.finish(r);
            });
        } catch(Throwable e) {
            log.error("autoRun operator throwable {}", take.getType(), e);
            take.finish(ErrorCode.UNKNOW_ERROR);
        }
    }

    protected abstract CompletableFuture<UtopiaErrorCodeClass> operator(Type type, SelfCirculationOperator selfCirculationOperator);
    protected abstract void idle();
}
