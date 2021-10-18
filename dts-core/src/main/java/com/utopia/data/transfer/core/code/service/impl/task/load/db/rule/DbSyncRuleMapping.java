package com.utopia.data.transfer.core.code.service.impl.task.load.db.rule;

import com.utopia.data.transfer.core.code.service.impl.task.load.db.SyncRuleTemplate;
import com.utopia.data.transfer.core.code.src.dialect.DbDialect;
import com.utopia.data.transfer.model.code.data.media.SyncRuleTarget;
import com.utopia.data.transfer.model.code.transfer.TransferUniqueDesc;
import com.utopia.string.UtopiaStringUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

        private final DbDialect dbDialect;
        private final SyncRuleTarget syncRuleTarget;
        private TransferUniqueDesc start = new TransferUniqueDesc();
        public DbSyncRuleMappingItem(Long pipelineId, DbDialect dbDialect, SyncRuleTarget syncRuleTarget) {
            this.dbDialect = dbDialect;
            this.syncRuleTarget = syncRuleTarget;
            //解析gtid
            StringBuilder sql = new StringBuilder(1024);
            sql.append("select gtid from ");
            DbRuleMapping.appendFullName(sql, syncRuleTarget.getNamespace(), syncRuleTarget.getValue());
            sql.append(" where pipeline_id = ");
            sql.append(pipelineId);

            List<String> query = dbDialect.getJdbcTemplate().query(sql.toString(), new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    if(rowNum > 0){
                        return rs.getString(0);
                    }
                    return null;
                }
            });
            if(!CollectionUtils.isEmpty(query)) {
                start.merge(TransferUniqueDesc.parseGtid(query.get(0)));
            }

            if(UtopiaStringUtil.isBlank(syncRuleTarget.getStartGtid())) {
                TransferUniqueDesc configDesc = TransferUniqueDesc.parseGtid(syncRuleTarget.getStartGtid());

            }
                if(UtopiaStringUtil.isBlank(syncRuleTarget.getStartGtid())){

                }
            }

        @Override
        public boolean filter(TransferUniqueDesc gtid) {
            return false;
        }

        @Override
        public void update(TransferUniqueDesc gtid) {

        }
    }
}
