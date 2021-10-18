package com.utopia.data.transfer.core.code.service;

import com.utopia.data.transfer.core.code.canal.CanalZKConfig;
import com.utopia.data.transfer.core.code.service.impl.task.LoadTaskImpl;
import com.utopia.data.transfer.core.code.service.impl.task.SelectTaskImpl;
import com.utopia.data.transfer.model.code.bean.StageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
@Service
public class TaskFactory {

    @Autowired
    private ConfigService configService;
    @Autowired
    private MessageParser messageParser;
    @Autowired
    private ArbitrateEventService arbitrateEventService;
    @Autowired
    private CanalZKConfig canalZKConfig;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 开始
     */
    public Task createTaskByType(StageType stageType){
        switch(stageType){
            case SELECT:
                return new SelectTaskImpl(configService, messageParser, arbitrateEventService, canalZKConfig);
            case LOAD:
                return new LoadTaskImpl(configService, messageParser, arbitrateEventService, applicationContext, applicationEventPublisher);
        }
        return null;
    }
}
