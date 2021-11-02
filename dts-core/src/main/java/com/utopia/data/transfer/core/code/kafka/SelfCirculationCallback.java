package com.utopia.data.transfer.core.code.kafka;

/**
 * @author owen.cai
 * @create_date 2021/11/2
 * @alter_author
 * @alter_date
 */
public interface SelfCirculationCallback {

    /**
     * 判断是否还运行
     * @return
     */
    boolean isRunning();

    /**
     * 提示有数据插入
     */
    void sign();
}
