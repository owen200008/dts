package com.utopia.data.transfer.core.code.src.dialect.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.utopia.data.transfer.core.archetype.base.ServiceException;
import com.utopia.data.transfer.core.code.base.ErrorCode;
import com.utopia.data.transfer.core.code.base.datasource.DataSourceService;
import com.utopia.data.transfer.core.code.base.datasource.bean.DataSourceItem;
import com.utopia.data.transfer.core.code.base.datasource.bean.db.DbMediaSource;
import com.utopia.data.transfer.core.code.src.dialect.DbDialect;
import com.utopia.data.transfer.core.code.src.dialect.DbDialectFactory;
import com.utopia.data.transfer.core.code.src.dialect.DbDialectHandler;
import com.utopia.extension.UtopiaExtensionLoader;
import com.utopia.log.BasicLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
@Slf4j
@Service
public class DbDialectFactoryImpl implements DbDialectFactory, DisposableBean {

    @Resource
    private DataSourceService dataSourceService;

    /**
     * 第一层pipelineId , 第二层DbMediaSource id
     */
    private LoadingCache<Long, LoadingCache<DbMediaSource, DbDialect>> dialects;

    public DbDialectFactoryImpl(){
        this.dialects = CacheBuilder.newBuilder().softValues().removalListener((RemovalListener<Long, LoadingCache<DbMediaSource, DbDialect>>) result -> {
            if (result == null) {
                return;
            }
            LoadingCache<DbMediaSource, DbDialect> value = result.getValue();
            if (value == null) {
                return;
            }
            for (DbDialect dbDialect : value.asMap().values()) {
                dbDialect.destory();
            }
        }).build(new CacheLoader<Long, LoadingCache<DbMediaSource, DbDialect>>() {
            @Override
            public LoadingCache<DbMediaSource, DbDialect> load(Long pipelineId) throws Exception {
                return CacheBuilder.newBuilder().build(new CacheLoader<DbMediaSource, DbDialect>() {
                    @Override
                    public DbDialect load(DbMediaSource source) throws Exception {
                        DataSourceItem<DataSource> dataSource = dataSourceService.getDataSource(pipelineId, source);
                        final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource.getItem());
                        return (DbDialect) jdbcTemplate.execute((ConnectionCallback) c -> {
                            DatabaseMetaData meta = c.getMetaData();
                            String databaseName = meta.getDatabaseProductName();
                            DbDialectHandler extension = UtopiaExtensionLoader.getExtensionLoader(DbDialectHandler.class).getExtension(source.getType().name());
                            if(extension == null){
                                String error = BasicLogUtil.argBuild("DbDialectHandler no find type", JSON.toJSONString(source));
                                log.error(error);
                                throw new ServiceException(ErrorCode.NO_SUPPORT_DIALECT_TYPE.getCode(), error);
                            }

                            DbDialect dialect = extension.createDialect(jdbcTemplate, meta);
                            if (dialect == null) {
                                String error = BasicLogUtil.argBuild("no dialect for", databaseName);
                                log.error(error);
                                throw new ServiceException(ErrorCode.CREATE_DIALECT_ERROR.getCode(), error);
                            }

                            if (log.isInfoEnabled()) {
                                log.info(String.format("--- DATABASE: %s, SCHEMA: %s ---",
                                        databaseName,
                                        (dialect.getDefaultSchema() == null) ? dialect.getDefaultCatalog() : dialect.getDefaultSchema()));
                            }
                            return dialect;
                        });
                    }
                });
            }
        });
    }

    @Override
    public void destroy() throws Exception {
        Set<Long> pipelineIds = new HashSet<Long>(dialects.asMap().keySet());
        for (Long pipelineId : pipelineIds) {
            destory(pipelineId);
        }
    }

    @Override
    public DbDialect getDbDialect(Long pipelineId, DbMediaSource source) {
        try {
            return dialects.get(pipelineId).get(source);
        } catch (ExecutionException e) {
            String info = "getDbDialect error " + pipelineId + " " + JSON.toJSONString(source);
            log.error(info);
            throw new ServiceException(ErrorCode.GET_DB_DIALECT_ERROR.getCode(), info);
        }
    }

    @Override
    public void destory(Long pipelineId) {
        dialects.invalidate(pipelineId);
    }
}
