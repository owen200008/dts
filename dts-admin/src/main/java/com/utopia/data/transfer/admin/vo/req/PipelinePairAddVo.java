package com.utopia.data.transfer.admin.vo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PipelinePairAddVo {


    private Long pipelineId;

    private String sourceName;

    private String sourceNamespace;

    private String sourceTable;

    private String targetName;

    private  String targetNamespace;

    private String targetTable;

    private  String rule;
}
