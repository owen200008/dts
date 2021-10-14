package com.utopia.data.transfer.core.code.service.impl;

import com.utopia.data.transfer.model.code.DTSServiceConf;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.data.transfer.core.code.service.ConfigService;
import org.springframework.stereotype.Service;

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

    private Map<Long, Pipeline> mapPipeline;
    private Map<Long, EntityDesc> mapEntity;

    @Override
    public void reloadConf(DTSServiceConf object) {
        this.mapPipeline = object.getList().stream().collect(Collectors.toMap(Pipeline::getId, item -> item));
        this.mapEntity = object.getEntityDescs().stream().collect(Collectors.toMap(EntityDesc::getId, item -> item));
    }

    @Override
    public Pipeline getPipeline(Long pipelineId) {
        return mapPipeline.get(pipelineId);
    }

    @Override
    public EntityDesc getEntityDesc(Long entityId) {
        return mapEntity.get(entityId);
    }
}
