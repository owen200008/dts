package com.utopia.data.transfer.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.NamedThreadFactory;

import java.util.Objects;
import java.util.Stack;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * describe:
 *
 * @author lxy
 * @date 2021/10/22
 */
@Slf4j
public class ExecTask extends TimerTask {

    volatile int nThreads=4;
    Stack<Short> threadNos = new Stack<>();
    ExecutorService executor = new ThreadPoolExecutor(nThreads, nThreads,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue(3), new NamedThreadFactory("TimelineRawFileCopy"), new ThreadPoolExecutor.AbortPolicy());

    public void init(){
        for (int i = 0; i < nThreads; i++) {
            threadNos.push((short) i);
        }
    }
    volatile  int full = nThreads;
    @Override
    public void run() {
      startTask();
    }
    public void startTask() {
        //启动所有的剩余线程来执行任务
        int acquire = 0;

        synchronized (this){
            acquire = full;
            if (full > 0){
                full = 0;
            }
        }
        log.info("当前线程数量 :{}",acquire);
        for (int i = 0; i < acquire; i++) {
            executor.submit(() -> {
                Short threadIndex = 1;
                log.info("submit start index {}", threadIndex);
                try {
                    if (Objects.nonNull(threadIndex)) {
                        while (readDealData()) {
                            log.info("sreadDealData {}", threadIndex);
                        }
                    } else {
                        log.error("thread Over total ：{}",threadIndex);
                    }
                } catch (Exception e) {
                    log.error("submit exception", e);
                } finally {
                    log.info("submit finish index {}", threadIndex);
                    threadNos.push(threadIndex);
                    // 加到
                    synchronized (this){
                        full+=1;
                        log.info("full ++:{}",full);
                    }
                }
            });
        }
        log.info("添加结束，线程数量:{}",full);
        if (full == acquire && full > 0){
            nThreads = acquire;
        }


    }
    public boolean readDealData(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
