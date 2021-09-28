package com.utopia.data.transfer.core.code.service.impl;

import com.alibaba.otter.canal.instance.manager.model.Canal;
import com.utopia.data.transfer.core.code.bean.Pipeline;
import com.utopia.data.transfer.core.code.config.CanalConfig;
import com.utopia.data.transfer.core.code.config.PipelineConfig;
import com.utopia.data.transfer.core.code.service.ConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Resource
    PipelineConfig pipelineConfig;
    @Resource
    CanalConfig canalConfig;

    Map<Long, Pipeline> mapPipeline;
    Map<String, Canal> mapCanal;

    @PostConstruct
    public void init(){
        mapPipeline = pipelineConfig.getList().stream().collect(Collectors.toMap(Pipeline::getId, item -> item));
        mapCanal = canalConfig.getList().stream().collect(Collectors.toMap(Canal::getName, item -> item));
    }

    @Override
    public Pipeline getPipeline(Long pipelineId) {
        return mapPipeline.get(pipelineId);
    }

    @Override
    public Canal getCanal(String destination) {
        return mapCanal.get(destination);
    }
}
