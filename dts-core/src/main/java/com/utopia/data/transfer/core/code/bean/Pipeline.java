package com.utopia.data.transfer.core.code.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */
@Data
public class Pipeline implements Serializable {
    private Long                id;
    private String              name;

    private List<DataMediaPair> pairs;

    private PipelineParameter   params;
}
