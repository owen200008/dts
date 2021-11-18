package com.utopia.data.transfer.model.code;

import com.utopia.data.transfer.model.code.config.OperatorConfig;
import com.utopia.data.transfer.model.code.entity.EntityDesc;
import com.utopia.data.transfer.model.code.pipeline.Pipeline;
import com.utopia.string.UtopiaStringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author owen.cai
 * @create_date 2021/9/28
 * @alter_author
 * @alter_date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DTSServiceConf implements Serializable {

    /**
     * 操作参数
     */
    private OperatorConfig operatorConfig;

    private List<Pipeline> list;
    private List<EntityDesc> entityDescs;

    private List<String> jars;
    private String md5Data;

    public boolean checkIsValid () {
        return UtopiaStringUtil.isNotBlank(md5Data) && Objects.nonNull(list);
    }
}
