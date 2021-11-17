package com.utopia.data.transfer.core.extension.select;

import com.utopia.data.transfer.core.base.config.ConfigService;
import com.utopia.data.transfer.core.extension.select.canal.CanalEmbedSelector;
import com.utopia.data.transfer.core.extension.select.utils.MessageParser;
import com.utopia.data.transfer.model.code.entity.data.EventDataTransaction;
import com.utopia.data.transfer.model.code.entity.data.Message;
import com.utopia.data.transfer.core.extension.base.select.SelectDataRule;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.register.center.api.ZookeeperConfig;

import java.util.Optional;

/**
 * @author owen.cai
 * @create_date 2021/10/19
 * @alter_author
 * @alter_date
 */
public class MysqlSelectDataRule implements SelectDataRule {

    private CanalEmbedSelector canalSelector;
    void init(Long pipelineId, ConfigService configService, MessageParser messageParser, ZookeeperConfig zookeeperConfig){
        Pipeline pipeline = configService.getPipeline(pipelineId);
        canalSelector = new CanalEmbedSelector(pipelineId, configService, messageParser, configService.getEntityDesc(pipeline.getSourceEntityId()), zookeeperConfig);
        canalSelector.start();
    }

    @Override
    public boolean isStart() {
        return canalSelector.isStart();
    }

    @Override
    public void stop() {
        canalSelector.stop();
    }

    @Override
    public Optional<Message<EventDataTransaction>> selector() throws InterruptedException {
        return canalSelector.selector();
    }

    @Override
    public void ack(Long id) {
        canalSelector.ack(id);
    }

    @Override
    public void rollback() {
        canalSelector.rollback();
    }
}
