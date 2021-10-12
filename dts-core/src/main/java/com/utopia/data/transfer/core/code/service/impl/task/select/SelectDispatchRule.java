package com.utopia.data.transfer.core.code.service.impl.task.select;

import com.utopia.data.transfer.core.code.model.EventData;
import com.utopia.data.transfer.core.code.model.Message;
import com.utopia.extension.UtopiaSPI;
import com.utopia.utils.BooleanMutex;

/**
 * @author owen.cai
 * @create_date 2021/9/30
 * @alter_author
 * @alter_date
 */
@UtopiaSPI
public interface SelectDispatchRule {
    /**
     * 初始化
     * @param dispatchRuleParam
     */
    boolean init(String dispatchRuleParam);

    /**
     *
     * @param message
     * @return
     */
    BooleanMutex dispatch(Message<EventData> message);
}
