package com.utopia.data.transfer.core.extension.sync.rule;

import com.utopia.data.transfer.core.extension.base.dialect.DbDialect;
import com.utopia.data.transfer.core.extension.base.sync.rule.SyncRuleTemplate;
import com.utopia.data.transfer.core.extension.tools.utils.DbCreateSqlTemplate;
import com.utopia.data.transfer.model.code.data.media.SyncRuleTarget;
import com.utopia.data.transfer.model.code.transfer.TransferUniqueDesc;
import com.utopia.string.UtopiaStringUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.StatementCreatorUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author owen.cai
 * @create_date 2021/10/18
 * @alter_author
 * @alter_date
 */
public class DbSyncRuleMapping implements SyncRuleTemplate {

    @Override
    public SyncRuleItem createSyncItem(Long pipelineId, DbDialect dbDialect, SyncRuleTarget syncRuleTarget) {
        DbSyncRuleMappingItem dbSyncRuleMappingItem = new DbSyncRuleMappingItem(pipelineId, dbDialect, syncRuleTarget);
        return dbSyncRuleMappingItem;
    }

    public static class DbSyncRuleMappingItem implements SyncRuleItem{
        private final Long pipelineId;
        private final DbDialect dbDialect;
        private final SyncRuleTarget syncRuleTarget;
        private TransferUniqueDesc start = new TransferUniqueDesc();
        public DbSyncRuleMappingItem(Long pipelineId, DbDialect dbDialect, SyncRuleTarget syncRuleTarget) {
            this.pipelineId = pipelineId;
            this.dbDialect = dbDialect;
            this.syncRuleTarget = syncRuleTarget;
            //解析gtid
            StringBuilder sql = new StringBuilder(1024);
            sql.append("select gtid from ");
            DbCreateSqlTemplate.appendFullName(sql, syncRuleTarget.getNamespace(), syncRuleTarget.getValue());
            sql.append(" where pipeline_id = ");
            sql.append(pipelineId);

            String query = dbDialect.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<String>() {

                @Override
                public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                    while (rs.next()) {
                        return rs.getString("gtid");
                    }
                    return null;
                }
            });
            if(UtopiaStringUtil.isNotBlank((query))){
                start.merge(TransferUniqueDesc.parseGtid(query));
            }
            else if(query == null){
                //如果不存在插入一条空的
                StringBuilder insertSql = new StringBuilder(1024);
                insertSql.append("insert into ");
                DbCreateSqlTemplate.appendFullName(insertSql, syncRuleTarget.getNamespace(), syncRuleTarget.getValue());
                insertSql.append("(pipeline_id, gtid) values(?, '')");
                dbDialect.getJdbcTemplate().update(insertSql.toString(), ps->{
                    StatementCreatorUtils.setParameterValue(ps, 1, Types.BIGINT, null, this.pipelineId.intValue());
                });
            }

            if(!UtopiaStringUtil.isBlank(syncRuleTarget.getStartGtid())) {
                start.merge(TransferUniqueDesc.parseGtid(syncRuleTarget.getStartGtid()));
            }
        }

        @Override
        public boolean filter(TransferUniqueDesc gtid) {
            return start.filter(gtid);
        }

        @Override
        public void update(TransferUniqueDesc gtid) {
            //合并
            start.merge(gtid);

            StringBuilder sql = new StringBuilder(1024);
            sql.append("update ");
            DbCreateSqlTemplate.appendFullName(sql, syncRuleTarget.getNamespace(), syncRuleTarget.getValue());
            sql.append(" set gtid = ? where pipeline_id = ?");
            //更新数据库
            dbDialect.getJdbcTemplate().update(sql.toString(), ps->{
                StatementCreatorUtils.setParameterValue(ps, 1, Types.VARCHAR, null, start.createUniqueWriteString());
                StatementCreatorUtils.setParameterValue(ps, 2, Types.BIGINT, null, this.pipelineId.intValue());
            });
        }
    }
}
