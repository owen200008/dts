package com.utopia.data.transfer.core.extension.base.select;

import com.utopia.extension.UtopiaSPI;

/**
 * @author owen.cai
 * @create_date 2021/10/19
 * @alter_author
 * @alter_date
 */
@UtopiaSPI
public interface SelectDataFactory {

    SelectDataRule createSelectDataRule(Long pipelineId);
}
