package com.utopia.data.transfer.core.code.service.impl.task.dispatch;

import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.extension.UtopiaSPI;
import com.utopia.model.rsp.UtopiaResponseModel;

/**
 * @author owen.cai
 * @create_date 2021/10/21
 * @alter_author
 * @alter_date
 */
@UtopiaSPI
public interface DispatchFactory {

    UtopiaResponseModel FACADE_ERROR = UtopiaResponseModel.fail(ErrorCode.SELECT_DISPATH_FACADE_NOFIND);
    UtopiaResponseModel EXCEPTION_ERROR = UtopiaResponseModel.fail(ErrorCode.SELECT_DISPATH_EXCEPTION);

    /**
     *
     * @param dispatchRuleParam
     * @return
     */
    SelectDispatchRule createSelectDispatchRule(String dispatchRuleParam);

    /**
     *
     * @param dispatchRuleParam
     * @return
     */
    LoadDispatchRule createLoadDispatchRule(String dispatchRuleParam);
}
