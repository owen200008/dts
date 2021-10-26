package com.utopia.data.transfer.admin.controller;

import java.util.Timer;

/**
 * describe:
 *
 * @author lxy
 * @date 2021/10/22
 */
public class ExecExample {

    public static void main(String[] args) throws InterruptedException {
        ExecTask execTask = new ExecTask();
        execTask.init();
        Timer t = new Timer();
        t.scheduleAtFixedRate(execTask, 100, 5000);


        Thread.sleep(2000);


        for (int i=0;i<111;i++){
            execTask.startTask();
            Thread.sleep(1000);
        }


        Thread.currentThread().join();

    }
}
