package com.utopia.data.transfer.core.code.service.impl.task.load.db;

import lombok.Data;

import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/10/11
 * @alter_author
 * @alter_date
 */
@Data
public class EvaluateClosureParam {
    private List<String> params;
    private String query;
}
