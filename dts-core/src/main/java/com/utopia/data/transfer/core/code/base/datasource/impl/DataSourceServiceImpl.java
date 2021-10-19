package com.utopia.data.transfer.core.code.base.datasource.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.*;
import com.utopia.data.transfer.model.archetype.ServiceException;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.core.code.base.datasource.DataSourceHandler;
import com.utopia.data.transfer.core.code.base.datasource.DataSourceService;
import com.utopia.data.transfer.core.code.base.datasource.bean.DataSourceItem;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.extension.UtopiaExtensionLoader;
import com.utopia.log.BasicLogUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
@Slf4j
@Service
public class DataSourceServiceImpl implements DataSourceService, DisposableBean {

    private LoadingCache<Long, LoadingCache<EntityDesc, DataSourceItem>> dataSources;

    public DataSourceServiceImpl(){
        this.dataSources = CacheBuilder.newBuilder().removalListener((RemovalListener<Long, LoadingCache<EntityDesc, DataSourceItem>>) result -> {
            if (result == null) {
                return;
            }
            LoadingCache<EntityDesc, DataSourceItem> value = result.getValue();
            if (value == null) {
                return;
            }
            value.asMap().forEach((key, item)->{
                try {
                    DataSourceHandler extension = UtopiaExtensionLoader.getExtensionLoader(DataSourceHandler.class).getExtension(key.getType().name());
                    extension.destory(item);
                } catch (SQLException e) {
                    log.error("ERROR ## close the datasource has an error", e);
                }
            });
        }).build(new CacheLoader<Long, LoadingCache<EntityDesc, DataSourceItem>>() {
            @Override
            public LoadingCache<EntityDesc, DataSourceItem> load(Long pipelineId) throws Exception {
                return CacheBuilder.newBuilder().build(new CacheLoader<EntityDesc, DataSourceItem>() {
                    @Override
                    public DataSourceItem load(EntityDesc entityDesc) {
                        DataSourceHandler extension = UtopiaExtensionLoader.getExtensionLoader(DataSourceHandler.class).getExtension(entityDesc.getType().name());
                        if(extension == null){
                            String createDataSource_error = BasicLogUtil.argBuild("DataSourceHandler no find type", JSON.toJSONString(entityDesc));
                            log.error(createDataSource_error);
                            throw new ServiceException(ErrorCode.NO_SUPPORT_DATASOURCE_TYPE.getCode(), createDataSource_error);
                        }
                        return extension.create(entityDesc);
                    }
                });
            }
        });
    }

    @Override
    public <T> DataSourceItem<T> getDataSource(long pipelineId, EntityDesc entityDesc) {
        try {
            return dataSources.get(pipelineId).get(entityDesc);
        } catch (ExecutionException e) {
            String getDataSource_exception = BasicLogUtil.argBuild("getDataSource exception", e);
            log.error(getDataSource_exception);
            throw new ServiceException(ErrorCode.GET_DATASOURCE_ERROR.getCode(), getDataSource_exception);
        }
    }

    @Override
    public void closePipeline(Long pipelineId) {
        dataSources.invalidate(pipelineId);
    }

    @Override
    public void destroy() throws Exception {
        for (Long pipelineId : dataSources.asMap().keySet()) {
            closePipeline(pipelineId);
        }
    }
}
