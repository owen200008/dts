package com.utopia.data.transfer.core.code.service.impl.task.load;

import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;

/**
 * @author owen.cai
 * @create_date 2021/10/13
 * @alter_author
 * @alter_date
 */
public interface LoadContext {

    /**
     * 获取pipeline
     * @return
     */
    Pipeline getPipeline();

    /**
     * 获取targetentity
     * @return
     */
    EntityDesc getTargetEntityDesc();
}
