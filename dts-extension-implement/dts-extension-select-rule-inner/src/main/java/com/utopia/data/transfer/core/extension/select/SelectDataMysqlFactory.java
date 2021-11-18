package com.utopia.data.transfer.core.extension.select;

import com.utopia.data.transfer.core.base.config.ConfigService;
import com.utopia.data.transfer.core.extension.base.dialect.DbDialectFactory;
import com.utopia.data.transfer.core.extension.base.select.SelectDataFactory;
import com.utopia.data.transfer.core.extension.base.select.SelectDataRule;
import com.utopia.data.transfer.core.extension.select.utils.MessageParser;
import com.utopia.extension.UtopiaSPIInitAfterInject;
import com.utopia.extension.UtopiaSPIInject;
import com.utopia.register.center.api.ZookeeperConfig;

/**
 * @author owen.cai
 * @create_date 2021/10/19
 * @alter_author
 * @alter_date
 */
public class SelectDataMysqlFactory implements SelectDataFactory {
    @UtopiaSPIInject
    private ConfigService configService;
    @UtopiaSPIInject
    private ZookeeperConfig zookeeperConfig;
    @UtopiaSPIInject
    private DbDialectFactory dbDialectFactory;

    private MessageParser messageParser;

    @UtopiaSPIInitAfterInject
    public void init() {
        this.messageParser = new MessageParser(configService, dbDialectFactory);
    }

    @Override
    public SelectDataRule createSelectDataRule(Long pipelineId) {
        MysqlSelectDataRule mysqlSelectDataRule = new MysqlSelectDataRule();
        mysqlSelectDataRule.init(pipelineId, configService, messageParser, zookeeperConfig);
        return mysqlSelectDataRule;
    }
}
