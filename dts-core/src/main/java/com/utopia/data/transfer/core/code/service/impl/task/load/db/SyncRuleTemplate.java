package com.utopia.data.transfer.core.code.service.impl.task.load.db;

import com.utopia.data.transfer.model.archetype.ServiceException;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.core.code.src.dialect.DbDialect;
import com.utopia.data.transfer.model.code.data.media.SyncRuleTarget;
import com.utopia.data.transfer.model.code.transfer.TransferUniqueDesc;
import com.utopia.extension.UtopiaExtensionLoader;
import com.utopia.extension.UtopiaSPI;

import java.util.Objects;

/**
 * @author owen.cai
 * @create_date 2021/10/18
 * @alter_author
 * @alter_date
 */
@UtopiaSPI
public interface SyncRuleTemplate {

    public interface SyncRuleItem{

        /**
         * 是否过滤
         * @param gtid
         * @return
         */
        boolean filter(TransferUniqueDesc gtid);

        /**
         * 更新
         * @param gtid
         */
        void update(TransferUniqueDesc gtid);
    }

    public static SyncRuleItem create(Long pipelineId, DbDialect dbDialect, SyncRuleTarget syncRuleTarget){
        SyncRuleTemplate extension = UtopiaExtensionLoader.getExtensionLoader(SyncRuleTemplate.class).getExtension(syncRuleTarget.getSyncRuleType().name());
        if(Objects.isNull(extension)){
            throw new ServiceException(ErrorCode.LOAD_CREATE_SYNCRULE_ERR);
        }
        return extension.createSyncItem(pipelineId, dbDialect, syncRuleTarget);
    }
    SyncRuleItem createSyncItem(Long pipelineId, DbDialect dbDialect, SyncRuleTarget syncRuleTarget);
}
