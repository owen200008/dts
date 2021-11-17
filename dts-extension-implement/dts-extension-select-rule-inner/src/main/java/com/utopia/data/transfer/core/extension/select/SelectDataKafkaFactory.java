package com.utopia.data.transfer.core.extension.select;

import com.utopia.data.transfer.core.base.config.ConfigService;
import com.utopia.data.transfer.core.extension.base.select.SelectDataFactory;
import com.utopia.data.transfer.core.extension.base.select.SelectDataRule;
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
