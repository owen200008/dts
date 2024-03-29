package com.utopia.data.transfer.core.extension.db.rule;

import com.utopia.data.transfer.core.extension.base.db.rule.DbRuleTemplate;
import com.utopia.data.transfer.core.extension.base.db.rule.SqlRunTemplate;
import com.utopia.data.transfer.core.extension.tools.utils.DbCreateSqlTemplate;
import com.utopia.data.transfer.model.code.data.media.DataMediaRulePair;
import com.utopia.data.transfer.model.code.entity.EventColumn;
import com.utopia.data.transfer.model.code.transfer.EventDataInterface;
import com.utopia.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.utopia.data.transfer.core.extension.tools.utils.DbCreateSqlTemplate.SQL_AND;
import static com.utopia.data.transfer.core.extension.tools.utils.DbCreateSqlTemplate.SQL_SPLITE;


/**
 * @author owen.cai
 * @create_date 2021/10/14
 * @alter_author
 * @alter_date
 */
public class DbRuleMapping implements DbRuleTemplate {


    @Override
    public List<SqlRunTemplate> create(DataMediaRulePair pair, EventDataInterface data) {
        List<SqlRunTemplate> ret = new ArrayList<>();
        if(data.getEventType().isInsert()){
            //insert
            List<EventColumn> columns = new ArrayList<>();
            StringBuilder sql = new StringBuilder(1024);
            sql.append("insert into ");
            appendFullName(sql, pair);
            sql.append("(");
            boolean bExistBefore = false;
            if(!CollectionUtils.isEmpty(data.getKeys())){
                appendColumnName(false, sql, data.getKeys());
                columns.addAll(data.getKeys());
                bExistBefore = true;
            }
            if(!CollectionUtils.isEmpty(data.getColumns())){
                appendColumnName(bExistBefore, sql, data.getColumns());
                columns.addAll(data.getColumns());
            }
            sql.append(") values (");
            appendColumnQuestions(sql, columns);
            sql.append(")");
            ret.add(SqlRunTemplate.builder()
                    .sql(sql.toString())
                    .columns(columns)
                    .build());
        }
        else if(data.getEventType().isDelete()){
            //delete
            List<EventColumn> columns = new ArrayList<>();
            StringBuilder sql = new StringBuilder(1024);
            sql.append("delete from ");
            appendFullName(sql, pair);
            sql.append(" where ");
            if(CollectionUtils.isEmpty(data.getKeys())){
                //无主键删除，存在null的情况
                appendColumnEquals(sql, data.getColumns(), SQL_AND, columns);
            }
            else{
                //有主键删除
                appendColumnEquals(sql, data.getKeys(), SQL_AND, columns);
            }
            ret.add(SqlRunTemplate.builder()
                    .sql(sql.toString())
                    .columns(columns)
                    .build());
        }
        else if(data.getEventType().isUpdate()){
            //update
            boolean existOldKeys = !CollectionUtils.isEmpty(data.getOldKeys());

            List<EventColumn> columns = new ArrayList<>();
            StringBuilder sql = new StringBuilder(1024);
            sql.append("update ");
            appendFullName(sql, pair);
            sql.append(" set ");
            appendColumnSet(false, sql, data.getColumns(), SQL_SPLITE, columns);
            if(existOldKeys && !CollectionUtils.isEmpty(data.getKeys())){
                appendColumnSet(data.getColumns().size() > 0, sql, data.getKeys(), SQL_SPLITE, columns);
            }
            sql.append(" where (");
            if(existOldKeys){
                appendColumnEquals(sql, data.getOldKeys(), SQL_AND, columns);
            }
            else{
                appendColumnEquals(sql, data.getKeys(), SQL_AND, columns);
            }
            sql.append(")");
            ret.add(SqlRunTemplate.builder()
                    .sql(sql.toString())
                    .columns(columns)
                    .build());
        }

        return ret;
    }

    protected void appendFullName(StringBuilder sql, DataMediaRulePair pair){
        DbCreateSqlTemplate.appendFullName(sql, pair.getTarget().getNamespace(), pair.getTarget().getValue());
    }

    protected void appendColumnSet(boolean existBefore, StringBuilder sql, List<EventColumn> columns, String separator, List<EventColumn> setColumns) {
        int size = columns.size();
        if(size > 0 && existBefore){
            sql.append(separator);
        }
        for (int i = 0; i < size; i++) {
            EventColumn eventColumn = columns.get(i);
            if(eventColumn.isNull()){
                sql.append(" `").append(columns.get(i).getColumnName()).append("` = null ");
            }
            else{
                sql.append(" `").append(columns.get(i).getColumnName()).append("` = ").append("? ");
                setColumns.add(eventColumn);
            }
            if (i != size - 1) {
                sql.append(separator);
            }
        }
    }

    protected void appendColumnEquals(StringBuilder sql, List<EventColumn> columns, String separator, List<EventColumn> setColumns) {
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            EventColumn eventColumn = columns.get(i);
            if(eventColumn.isNull()){
                sql.append(" `").append(columns.get(i).getColumnName()).append("` is null ");
            }
            else{
                sql.append(" `").append(columns.get(i).getColumnName()).append("` = ").append("? ");
                setColumns.add(eventColumn);
            }
            if (i != size - 1) {
                sql.append(separator);
            }
        }
    }

    protected void appendColumnName(boolean existBefore, StringBuilder sql, List<EventColumn> columns) {
        int size = columns.size();
        if(size > 0 && existBefore){
            sql.append(" , ");
        }
        for (int i = 0; i < size; i++) {
            sql.append("`").append(columns.get(i).getColumnName()).append("`").append((i + 1 < size) ? " , " : "");
        }
    }

    protected void appendColumnQuestions(StringBuilder sql, List<EventColumn> columns) {
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            sql.append("?").append((i + 1 < size) ? " , " : "");
        }
    }
}
