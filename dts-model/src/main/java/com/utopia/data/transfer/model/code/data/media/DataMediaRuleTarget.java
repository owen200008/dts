package com.utopia.data.transfer.model.code.data.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataMediaRuleTarget implements Serializable {
    /**
     * 唯一描述表信息
     */
    private long                id;

    private DataMediaRuleType   dataMediaRuleType = DataMediaRuleType.DEFAULT;
    private String              namespace;
    private String              value;
}
