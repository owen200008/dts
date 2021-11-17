package com.utopia.data.transfer.core.code.service;

import com.utopia.data.transfer.core.base.config.ConfigService;
import com.utopia.data.transfer.core.code.service.impl.task.LoadTaskImpl;
import com.utopia.data.transfer.core.code.service.impl.task.SelectTaskImpl;
import com.utopia.data.transfer.model.code.bean.StageType;
import com.utopia.data.transfer.model.code.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ArbitrateEventService arbitrateEventService;

    /**
     * 开始
     */
    public Task createTaskByType(StageType stageType){
        switch(stageType){
            case SELECT:
                return new SelectTaskImpl(configService, arbitrateEventService);
            case LOAD:
                return new LoadTaskImpl(configService, arbitrateEventService);
        }
        return null;
    }
}
