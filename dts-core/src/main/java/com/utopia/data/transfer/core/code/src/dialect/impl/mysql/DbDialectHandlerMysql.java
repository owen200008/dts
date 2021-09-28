package com.utopia.data.transfer.core.code.src.dialect.impl.mysql;

import com.utopia.data.transfer.core.code.src.dialect.DbDialect;
import com.utopia.data.transfer.core.code.src.dialect.DbDialectHandler;
import com.utopia.extension.UtopiaSPIInject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
public class DbDialectHandlerMysql implements DbDialectHandler {
    @UtopiaSPIInject
    protected LobHandler defaultLobHandler;

    @Override
    public DbDialect createDialect(JdbcTemplate jdbcTemplate, DatabaseMetaData meta) throws SQLException {
        return new MysqlDialect(jdbcTemplate,
                defaultLobHandler,
                meta.getDatabaseProductName(),
                meta.getDatabaseProductVersion(),
                meta.getDatabaseMajorVersion(),
                meta.getDatabaseMinorVersion());

    }
}
