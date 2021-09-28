package com.utopia.data.transfer.core.code.src.dialect;

import com.utopia.extension.UtopiaSPI;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
@UtopiaSPI
public interface DbDialectHandler {

    /**
     * 创建方言
     * @param jdbcTemplate
     * @param meta
     * @return
     */
    DbDialect createDialect(JdbcTemplate jdbcTemplate, DatabaseMetaData meta) throws SQLException;
}
