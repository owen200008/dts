package com.utopia.data.transfer.core.extension.select.kafka;

/**
 * @author owen.cai
 * @create_date 2021/11/2
 * @alter_author
 * @alter_date
 */
public interface SelfCirculationOperator<RESULT> {
    void setResult(RESULT result);
}
