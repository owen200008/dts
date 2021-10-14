package com.utopia.data.transfer.core.code.service.impl.task.load.db.rule;

import com.utopia.data.transfer.core.code.service.impl.task.load.db.DbRuleTemplate;
import com.utopia.data.transfer.core.code.service.impl.task.load.db.SqlRunTemplate;
import com.utopia.data.transfer.model.code.data.media.DataMediaRulePair;
import com.utopia.data.transfer.model.code.entity.EventColumn;
import com.utopia.data.transfer.model.code.transfer.TransferEventData;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/10/14
 * @alter_author
 * @alter_date
 */
public class DbRuleMapping implements DbRuleTemplate {

    private static final String DOT = ".";
    private static final String SQL_AND = "and";
    private static final String SQL_SPLITE = ",";

    @Override
    public List<SqlRunTemplate> create(DataMediaRulePair pair, TransferEventData data) {
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
            appendColumnSet(sql, data.getColumns(), SQL_SPLITE, columns);
            if(existOldKeys && !CollectionUtils.isEmpty(data.getKeys())){
                appendColumnSet(sql, data.getKeys(), SQL_SPLITE, columns);
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
        if (pair.getTarget().getNamespace() != null) {
            sql.append(pair.getTarget().getNamespace()).append(DOT);
        }
        sql.append(pair.getTarget().getValue());
    }


    protected void appendColumnSet(StringBuilder sql, List<EventColumn> columns, String separator, List<EventColumn> setColumns) {
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            EventColumn eventColumn = columns.get(i);
            if(eventColumn.isNull()){
                sql.append(" ").append(columns.get(i).getColumnName()).append(" = null ");
            }
            else{
                sql.append(" ").append(columns.get(i).getColumnName()).append(" = ").append("? ");
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
                sql.append(" ").append(columns.get(i).getColumnName()).append(" is null ");
            }
            else{
                sql.append(" ").append(columns.get(i).getColumnName()).append(" = ").append("? ");
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
            sql.append(columns.get(i).getColumnName()).append((i + 1 < size) ? " , " : "");
        }
    }

    protected void appendColumnQuestions(StringBuilder sql, List<EventColumn> columns) {
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            sql.append("?").append((i + 1 < size) ? " , " : "");
        }
    }
}
