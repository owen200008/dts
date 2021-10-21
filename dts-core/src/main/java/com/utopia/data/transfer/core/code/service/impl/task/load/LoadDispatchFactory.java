package com.utopia.data.transfer.core.code.service.impl.task.load;

import com.utopia.extension.UtopiaSPI;

/**
 * @author owen.cai
 * @create_date 2021/10/21
 * @alter_author
 * @alter_date
 */
@UtopiaSPI
public interface LoadDispatchFactory {
    /**
     * load的任务
     * @param dispatchRuleParam
     */
    LoadDispatchRule create(String dispatchRuleParam);
}
