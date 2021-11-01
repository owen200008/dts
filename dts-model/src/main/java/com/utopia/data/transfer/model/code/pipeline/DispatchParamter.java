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
public class DispatchParamter implements Serializable {
    private String dispatchRule = "INNER";
    private Boolean transferFullData = false;
    private String dispatchSelectParam = "";
    private String dispatchLoadParam = "";
}
