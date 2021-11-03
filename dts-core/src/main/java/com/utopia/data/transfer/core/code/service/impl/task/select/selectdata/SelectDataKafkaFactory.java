package com.utopia.data.transfer.core.code.service.impl.task.select.selectdata;

import com.utopia.data.transfer.core.code.service.ConfigService;
import com.utopia.data.transfer.core.code.service.impl.task.select.SelectDataFactory;
import com.utopia.data.transfer.core.code.service.impl.task.select.SelectDataRule;
import com.utopia.extension.UtopiaSPIInject;

/**
 * @author owen.cai
 * @create_date 2021/10/19
 * @alter_author
 * @alter_date
 */
public class SelectDataKafkaFactory implements SelectDataFactory {
    @UtopiaSPIInject
    private ConfigService configService;

    @Override
    public SelectDataRule createSelectDataRule(Long pipelineId) {
        KafkaSelectDataRule mysqlSelectDataRule = new KafkaSelectDataRule();
        mysqlSelectDataRule.init(pipelineId, configService);
        return mysqlSelectDataRule;
    }
}
