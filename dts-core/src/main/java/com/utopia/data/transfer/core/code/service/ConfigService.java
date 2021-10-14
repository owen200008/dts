package com.utopia.data.transfer.core.code.service;

import com.utopia.data.transfer.model.code.DTSServiceConf;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
public interface ConfigService {


    /**
     * 重新加载
     * @param object
     */
    void reloadConf(DTSServiceConf object);

    /**
     * 获取pipeline配置
     * @param pipelineId
     * @return
     */
    Pipeline getPipeline(Long pipelineId);

    /**
     * 获取canal配置
     * @param entityId
     * @return
     */
    EntityDesc getEntityDesc(Long entityId);

}
