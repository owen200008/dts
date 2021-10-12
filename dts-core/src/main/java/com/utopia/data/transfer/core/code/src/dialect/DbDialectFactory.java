package com.utopia.data.transfer.core.code.src.dialect;


import com.utopia.data.transfer.model.code.data.media.DataMediaSource;


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
    DbDialect getDbDialect(Long pipelineId, DataMediaSource source);

    /**
     *
     * @param pipelineId
     */
    void closePipeline(Long pipelineId);
}
