package com.utopia.data.transfer.core.code.service.impl.task.select.selectdata;

import com.utopia.data.transfer.core.code.service.ConfigService;
import com.utopia.data.transfer.core.code.service.MessageParser;
import com.utopia.data.transfer.core.code.service.impl.task.select.SelectDataFactory;
import com.utopia.data.transfer.core.code.service.impl.task.select.SelectDataRule;
import com.utopia.extension.UtopiaSPIInject;
import com.utopia.register.center.api.ZookeeperConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

/**
 * @author owen.cai
 * @create_date 2021/10/19
 * @alter_author
 * @alter_date
 */
public class SelectDataKafkaFactory implements SelectDataFactory {
    @UtopiaSPIInject
    private ConfigService configService;
    @UtopiaSPIInject
    private KafkaProperties kafkaProperties;

    @Override
    public SelectDataRule createSelectDataRule(Long pipelineId) {
        KafkaSelectDataRule mysqlSelectDataRule = new KafkaSelectDataRule();
        mysqlSelectDataRule.init(pipelineId, configService, kafkaProperties);
        return mysqlSelectDataRule;
    }
}
