package com.utopia.data.transfer.core.code.service.impl.task.load.db;

import com.utopia.data.transfer.model.code.data.media.DataMediaRulePair;
import com.utopia.data.transfer.model.code.transfer.EventDataInterface;
import com.utopia.extension.UtopiaSPI;

import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/10/14
 * @alter_author
 * @alter_date
 */
@UtopiaSPI
public interface DbRuleTemplate {

    List<SqlRunTemplate> create(DataMediaRulePair pair, EventDataInterface data);
}
