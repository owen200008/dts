package com.utopia.data.transfer.core.code.src.dialect.impl.mysql;

import com.utopia.data.transfer.core.code.src.dialect.impl.AbstractDbDialect;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
public class MysqlDialect extends AbstractDbDialect {
    public MysqlDialect(JdbcTemplate jdbcTemplate, LobHandler lobHandler){
        super(jdbcTemplate, lobHandler);
    }
    public MysqlDialect(JdbcTemplate jdbcTemplate, LobHandler lobHandler, String name, String databaseVersion,
                        int majorVersion, int minorVersion){
        super(jdbcTemplate, lobHandler, name, majorVersion, minorVersion);
    }

    @Override
    public String getDefaultSchema() {
        return null;
    }

    @Override
    public String getDefaultCatalog() {
        return (String) jdbcTemplate.queryForObject("select database()", String.class);
    }

    @Override
    public boolean isCharSpacePadded() {
        return false;
    }

    @Override
    public boolean isCharSpaceTrimmed() {
        return true;
    }

    @Override
    public boolean isEmptyStringNulled() {
        return false;
    }

    @Override
    public boolean isSupportMergeSql() {
        return true;
    }
}
