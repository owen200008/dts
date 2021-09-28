package com.utopia.data.transfer.core.code.service;

import com.alibaba.otter.canal.instance.manager.model.Canal;
import com.utopia.data.transfer.core.code.bean.Pipeline;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
public interface ConfigService {

    /**
     * 获取pipeline配置
     * @param pipelineId
     * @return
     */
    Pipeline getPipeline(Long pipelineId);

    /**
     * 获取canal配置
     * @param destination
     * @return
     */
    Canal getCanal(String destination);
}
