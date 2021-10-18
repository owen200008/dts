package com.utopia.data.transfer.model.code.data.media;

import lombok.Data;

/**
 * @author owen.cai
 * @create_date 2021/10/15
 * @alter_author
 * @alter_date
 */
@Data
public class SyncRuleTarget {
    private SyncRuleType        syncRuleType = SyncRuleType.DEFAULT;
    private String              namespace;
    private String              value;
    private String              startGtid;
}
