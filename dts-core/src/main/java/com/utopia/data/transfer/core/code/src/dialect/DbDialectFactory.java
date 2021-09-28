package com.utopia.data.transfer.core.code.src.dialect;


import com.utopia.data.transfer.core.code.base.datasource.bean.db.DbMediaSource;


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
    DbDialect getDbDialect(Long pipelineId, DbMediaSource source);

    /**
     *
     * @param pipelineId
     */
    void destory(Long pipelineId);
}
