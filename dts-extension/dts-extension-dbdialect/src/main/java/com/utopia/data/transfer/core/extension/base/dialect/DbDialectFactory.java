package com.utopia.data.transfer.core.extension.base.dialect;

import com.utopia.data.transfer.model.code.entity.EntityDesc;


/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */

public interface DbDialectFactory {

    /**
     *
     * @param pipelineId
     * @param source
     * @return
     */
    DbDialect getDbDialect(Long pipelineId, EntityDesc source);

    /**
     *
     * @param pipelineId
     */
    void closePipeline(Long pipelineId);
}
