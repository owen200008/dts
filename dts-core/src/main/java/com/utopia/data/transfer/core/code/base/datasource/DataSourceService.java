package com.utopia.data.transfer.core.code.base.datasource;

import com.utopia.data.transfer.core.code.base.datasource.bean.DataMediaSource;
import com.utopia.data.transfer.core.code.base.datasource.bean.DataSourceItem;

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
     * @param dataMediaSource
     * @return
     */
    <T> DataSourceItem<T> getDataSource(long pipelineId, DataMediaSource dataMediaSource);

    /**
     * 释放当前pipeline的数据源.
     *
     * @param pipelineId
     */
    void destroy(Long pipelineId);
}
