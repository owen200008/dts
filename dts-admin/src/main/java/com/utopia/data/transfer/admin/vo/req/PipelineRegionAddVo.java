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
public class PipelineRegionAddVo {


    private Integer pipelineId;

    private String sourceRegion;

    private String targetRegion;

}
