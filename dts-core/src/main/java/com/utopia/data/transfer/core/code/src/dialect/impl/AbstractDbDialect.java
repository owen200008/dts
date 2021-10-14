package com.utopia.data.transfer.core.code.src.dialect.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.utopia.data.transfer.core.archetype.base.ServiceException;
import com.utopia.data.transfer.core.code.base.ErrorCode;
import com.utopia.data.transfer.core.code.src.dialect.DbDialect;
import com.utopia.data.transfer.core.code.src.dialect.DdlUtils;
import com.utopia.data.transfer.core.code.src.dialect.SqlTemplate;
import com.utopia.log.BasicLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ddlutils.model.Table;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.sql.DatabaseMetaData;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
@Slf4j
public abstract class AbstractDbDialect implements DbDialect {

    protected int                      databaseMajorVersion;
    protected int                      databaseMinorVersion;
    protected String                   databaseName;
    protected JdbcTemplate             jdbcTemplate;
    protected LobHandler               lobHandler;
    protected TransactionTemplate      transactionTemplate;
    protected LoadingCache<List<String>, Table> tables;

    public AbstractDbDialect(final JdbcTemplate jdbcTemplate, LobHandler lobHandler){
        // 初始化一些数据
        jdbcTemplate.execute((ConnectionCallback) c -> {
            DatabaseMetaData meta = c.getMetaData();
            databaseName = meta.getDatabaseProductName();
            databaseMajorVersion = meta.getDatabaseMajorVersion();
            databaseMinorVersion = meta.getDatabaseMinorVersion();
            return null;
        });
        init(jdbcTemplate, lobHandler, databaseName, databaseMajorVersion, databaseMinorVersion);
    }

    public AbstractDbDialect(JdbcTemplate jdbcTemplate, LobHandler lobHandler, String name, int majorVersion,
                             int minorVersion){
        init(jdbcTemplate, lobHandler, name, majorVersion, minorVersion);
    }

    protected void init (JdbcTemplate jdbcTemplate, LobHandler lobHandler, String name, int majorVersion, int minorVersion){
        this.jdbcTemplate = jdbcTemplate;
        this.lobHandler = lobHandler;
        // 初始化transction
        this.transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(new DataSourceTransactionManager(this.jdbcTemplate.getDataSource()));
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        this.databaseName = name;
        this.databaseMajorVersion = majorVersion;
        this.databaseMinorVersion = minorVersion;

        initTables(this.jdbcTemplate);
    }

    private void initTables(final JdbcTemplate jdbcTemplate) {
        this.tables = CacheBuilder.newBuilder().softValues().build(new CacheLoader<List<String>, Table>() {
            @Override
            public Table load(List<String> names) throws Exception {
                Assert.isTrue(names.size() == 2, "db dialect names size != 2");
                try {
                    beforeFindTable(jdbcTemplate, names.get(0), names.get(0), names.get(1));
                    Table table = DdlUtils.findTable(jdbcTemplate, names.get(0), names.get(0), names.get(1));
                    afterFindTable(table, jdbcTemplate, names.get(0), names.get(0), names.get(1));
                    if (table == null) {
                        throw new ServiceException(ErrorCode.NO_FIND_TABLE.getCode(), "no found table [" + names.get(0) + "." + names.get(1)
                                + "] , pls check");
                    } else {
                        return table;
                    }
                } catch (Exception e) {
                    throw new ServiceException(ErrorCode.FIND_TABLE_ERROR.getCode(), "find table [" + names.get(0) + "." + names.get(1) + "] error",
                            e);
                }
            }
        });
    }

    protected void beforeFindTable(JdbcTemplate jdbcTemplate, String catalogName, String schemaName, String tableName) {
        // for subclass to extend
    }

    protected void afterFindTable(Table table, JdbcTemplate jdbcTemplate, String catalogName, String schemaName,
                                  String tableName) {
        // for subclass to extend
    }

    @Override
    public Table findTable(String schema, String table, boolean useCache) {
        List<String> key = Arrays.asList(schema, table);
        if (useCache == false) {
            tables.invalidate(key);
        }
        try {
            return tables.get(key);
        } catch (ExecutionException e) {
            String error = BasicLogUtil.argBuild("findTable exception", e);
            log.error(error);
            throw new ServiceException(ErrorCode.GET_TABLE_ERROR.getCode(), error);
        }
    }

    @Override
    public Table findTable(String schema, String table) {
        return findTable(schema, table, true);
    }

    @Override
    public void reloadTable(String schema, String table) {
        if (StringUtils.isNotEmpty(table)) {
            tables.invalidate(Arrays.asList(schema, table));
        } else {
            // 如果没有存在表名，则直接清空所有的table，重新加载
            tables.invalidateAll();
        }
    }

    @Override
    public String getName() {
        return databaseName;
    }

    @Override
    public int getMajorVersion() {
        return databaseMajorVersion;
    }

    @Override
    public int getMinorVersion() {
        return databaseMinorVersion;
    }

    @Override
    public String getVersion() {
        return databaseMajorVersion + "." + databaseMinorVersion;
    }

    @Override
    public LobHandler getLobHandler() {
        return lobHandler;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    @Override
    public void destory() {
    }
}
