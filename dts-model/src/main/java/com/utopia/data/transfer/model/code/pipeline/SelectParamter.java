package com.utopia.data.transfer.model.code.pipeline;

import lombok.Data;

import java.io.Serializable;

/**
 * @author owen.cai
 * @create_date 2021/9/30
 * @alter_author
 * @alter_date
 */
@Data
public class SelectParamter implements Serializable {
    private String dispatchRule;
    private String dispatchRuleParam;
}
