package com.utopia.data.transfer.core.code.src.dialect;

/**
 * @author owen.cai
 * @create_date 2021/9/24
 * @alter_author
 * @alter_date
 */
public abstract class AbstractSqlTemplate implements SqlTemplate {
    private static final String DOT = ".";

    @Override
    public String getSelectSql(String schemaName, String tableName, String[] pkNames, String[] columnNames) {
        StringBuilder sql = new StringBuilder("select ");
        int size = columnNames.length;
        for (int i = 0; i < size; i++) {
            sql.append(appendEscape(columnNames[i])).append((i + 1 < size) ? " , " : "");
        }

        sql.append(" from ").append(getFullName(schemaName, tableName)).append(" where ( ");
        appendColumnEquals(sql, pkNames, "and");
        sql.append(" ) ");
        return sql.toString();
    }

    @Override
    public String getUpdateSql(String schemaName, String tableName, String[] pkNames, String[] columnNames, boolean updatePks, String shardColumn) {
        StringBuilder sql = new StringBuilder("update " + getFullName(schemaName, tableName) + " set ");
        appendExcludeSingleShardColumnEquals(sql, columnNames, ",", updatePks, shardColumn);
        sql.append(" where (");
        appendColumnEquals(sql, pkNames, "and");
        sql.append(")");
        return sql.toString();
    }

    @Override
    public String getInsertSql(String schemaName, String tableName, String[] pkNames, String[] columnNames) {
        StringBuilder sql = new StringBuilder("insert into " + getFullName(schemaName, tableName) + "(");
        String[] allColumns = new String[pkNames.length + columnNames.length];
        System.arraycopy(columnNames, 0, allColumns, 0, columnNames.length);
        System.arraycopy(pkNames, 0, allColumns, columnNames.length, pkNames.length);

        int size = allColumns.length;
        for (int i = 0; i < size; i++) {
            sql.append(appendEscape(allColumns[i])).append((i + 1 < size) ? "," : "");
        }

        sql.append(") values (");
        appendColumnQuestions(sql, allColumns);
        sql.append(")");
        return sql.toString();
    }

    @Override
    public String getDeleteSql(String schemaName, String tableName, String[] pkNames) {
        StringBuilder sql = new StringBuilder("delete from " + getFullName(schemaName, tableName) + " where ");
        appendColumnEquals(sql, pkNames, "and");
        return sql.toString();
    }

    // ================ helper method ============

    protected String appendEscape(String columnName) {
        return columnName;
    }

    protected String getFullName(String schemaName, String tableName) {
        StringBuilder sb = new StringBuilder();
        if (schemaName != null) {
            sb.append(appendEscape(schemaName)).append(DOT);
        }
        sb.append(appendEscape(tableName));
        return sb.toString().intern();
    }

    protected void appendColumnEquals(StringBuilder sql, String[] columns, String separator) {
        int size = columns.length;
        for (int i = 0; i < size; i++) {
            sql.append(" ").append(appendEscape(columns[i])).append(" = ").append("? ");
            if (i != size - 1) {
                sql.append(separator);
            }
        }
    }

    /**
     * 针对DRDS改造, 在 update set 集合中, 排除 单个拆分键 的赋值操作
     * @param sql
     * @param columns
     * @param separator
     * @param excludeShardColumn 需要排除的 拆分列
     */
    protected void appendExcludeSingleShardColumnEquals(StringBuilder sql, String[] columns, String separator, boolean updatePks, String excludeShardColumn) {
        int size = columns.length;
        for (int i = 0; i < size; i++) {
            // 如果是DRDS数据库, 并且存在拆分键 且 等于当前循环列, 跳过
            if(!updatePks && excludeShardColumn != null && columns[i].equals(excludeShardColumn)){
                continue;
            }
            sql.append(" ").append(appendEscape(columns[i])).append(" = ").append("? ");
            if (i != size - 1) {
                sql.append(separator);
            }
        }
    }

    protected void appendColumnQuestions(StringBuilder sql, String[] columns) {
        int size = columns.length;
        for (int i = 0; i < size; i++) {
            sql.append("?").append((i + 1 < size) ? " , " : "");
        }
    }

}
