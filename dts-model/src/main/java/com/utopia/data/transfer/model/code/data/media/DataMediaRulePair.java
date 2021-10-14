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
public class DataMediaRulePair implements Serializable {
    private DataMediaRuleSource source;
    private DataMediaRuleTarget target;
}
