package com.utopia.data.transfer.core.code.base.datasource.impl;

import com.utopia.data.transfer.core.code.base.datasource.DataSourceHandler;
import com.utopia.data.transfer.core.code.base.datasource.bean.DataSourceItem;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.entity.mysql.MysqlProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
@Slf4j
public class DataSourceHandlerMysql implements DataSourceHandler {


    @Override
    public DataSourceItem create(EntityDesc entityDesc) {
        BasicDataSource dbcpDs = new BasicDataSource();

        MysqlProperty mysql = entityDesc.getParams().toJavaObject(MysqlProperty.class);

        // 初始化连接池时创建的连接数
        dbcpDs.setInitialSize(mysql.getInitialSize());
        // 连接池允许的最大并发连接数，值为非正数时表示不限制
        dbcpDs.setMaxActive(mysql.getMaxActive());
        // 连接池中的最大空闲连接数，超过时，多余的空闲连接将会被释放，值为负数时表示不限制
        dbcpDs.setMaxIdle(mysql.getMaxIdle());
        // 连接池中的最小空闲连接数，低于此数值时将会创建所欠缺的连接，值为0时表示不创建
        dbcpDs.setMinIdle(mysql.getMinIdle());
        // 以毫秒表示的当连接池中没有可用连接时等待可用连接返回的时间，超时则抛出异常，值为-1时表示无限等待
        dbcpDs.setMaxWait(mysql.getMaxWait());
        // 是否清除已经超过removeAbandonedTimeout设置的无效连接
        dbcpDs.setRemoveAbandoned(true);
        // 当清除无效链接时是否在日志中记录清除信息的标志
        dbcpDs.setLogAbandoned(true);
        // 以秒表示清除无效链接的时限
        dbcpDs.setRemoveAbandonedTimeout(mysql.getRemoveAbandonedTimeout());
        // 确保连接池中没有已破损的连接
        dbcpDs.setNumTestsPerEvictionRun(mysql.getNumTestsPerEvictionRun());
        // 指定连接被调用时是否经过校验
        dbcpDs.setTestOnBorrow(false);
        // 指定连接返回到池中时是否经过校验
        dbcpDs.setTestOnReturn(false);
        // 指定连接进入空闲状态时是否经过空闲对象驱逐进程的校验
        dbcpDs.setTestWhileIdle(true);
        // 以毫秒表示空闲对象驱逐进程由运行状态进入休眠状态的时长，值为非正数时表示不运行任何空闲对象驱逐进程
        dbcpDs.setTimeBetweenEvictionRunsMillis(mysql.getTimeBetweenEvictionRunsMillis());
        // 以毫秒表示连接被空闲对象驱逐进程驱逐前在池中保持空闲状态的最小时间
        dbcpDs.setMinEvictableIdleTimeMillis(mysql.getMinEvictableIdleTimeMillis());

        // 动态的参数
        dbcpDs.setDriverClassName(entityDesc.getDriver());
        dbcpDs.setUrl(entityDesc.getUrl());
        dbcpDs.setUsername(entityDesc.getUsername());
        dbcpDs.setPassword(entityDesc.getPassword());

        // open the batch mode for mysql since 5.1.8
        dbcpDs.addConnectionProperty("useServerPrepStmts", "false");
        dbcpDs.addConnectionProperty("rewriteBatchedStatements", "true");
        // 将0000-00-00的时间类型返回null
        dbcpDs.addConnectionProperty("zeroDateTimeBehavior", "convertToNull");
        // 直接返回字符串，不做year转换date处理
        dbcpDs.addConnectionProperty("yearIsDateType", "false");
        // 返回时间类型的字符串,不做时区处理
        dbcpDs.addConnectionProperty("noDatetimeStringSync", "true");
        // 允许sqlMode为非严格模式
        dbcpDs.addConnectionProperty("jdbcCompliantTruncation", "false");
        if (StringUtils.isNotEmpty(entityDesc.getEncode())) {
            if (StringUtils.equalsIgnoreCase(entityDesc.getEncode(), "utf8mb4")) {
                dbcpDs.addConnectionProperty("characterEncoding", "utf8");
                dbcpDs.setConnectionInitSqls(Arrays.asList("set names utf8mb4"));
            } else {
                dbcpDs.addConnectionProperty("characterEncoding", entityDesc.getEncode());
            }
        }
        dbcpDs.setValidationQuery("select 1");

        return new DataSourceItem(dbcpDs);
    }

    @Override
    public void destory(DataSourceItem dataSource) throws SQLException {
        DataSourceItem<DataSource> item = ((DataSourceItem<DataSource>) dataSource);

        // fallback for regular destroy
        // TODO need to integrate to handler
        BasicDataSource basicDataSource = (BasicDataSource) item.getItem();
        basicDataSource.close();
    }
}
