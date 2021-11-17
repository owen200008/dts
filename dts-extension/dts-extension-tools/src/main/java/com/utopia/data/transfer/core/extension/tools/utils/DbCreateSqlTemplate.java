package com.utopia.data.transfer.core.extension.tools.utils;

import com.utopia.string.UtopiaStringUtil;

/**
 * @author owen.cai
 * @create_date 2021/11/17
 * @alter_author
 * @alter_date
 */
public class DbCreateSqlTemplate {

    public static final String DOT = ".";
    public static final String SQL_AND = "and";
    public static final String SQL_SPLITE = ",";

    public static void appendFullName(StringBuilder sql, String namespace, String value){
        if(UtopiaStringUtil.isNotBlank(namespace)){
            sql.append(namespace).append(DOT);
        }
        sql.append(value);
    }
}
