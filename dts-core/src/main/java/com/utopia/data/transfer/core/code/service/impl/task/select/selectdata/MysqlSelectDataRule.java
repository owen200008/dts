package com.utopia.data.transfer.core.code.service.impl.task.select.selectdata;

import com.utopia.data.transfer.core.code.canal.CanalEmbedSelector;
import com.utopia.data.transfer.core.code.model.EventDataTransaction;
import com.utopia.data.transfer.core.code.model.Message;
import com.utopia.data.transfer.core.code.service.ConfigService;
import com.utopia.data.transfer.core.code.service.MessageParser;
import com.utopia.data.transfer.core.code.service.impl.task.select.SelectDataRule;
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
