package com.utopia.data.transfer.core.extension.base.datasource;

import com.utopia.data.transfer.model.code.entity.EntityDesc;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
public interface DataSourceService {
    /**
     * 返回操作数据源的句柄
     *
     * @param <T>
     * @param entityDesc
     * @return
     */
    <T> DataSourceItem<T> getDataSource(long pipelineId, EntityDesc entityDesc);

    /**
     * 释放当前pipeline的数据源.
     *
     * @param pipelineId
     */
    void closePipeline(Long pipelineId);
}
