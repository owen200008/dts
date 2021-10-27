package com.utopia.data.transfer.admin.vo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryRegionPipelineVo {

    private Long pipelineId;


    private Integer pageNum = 1;

    private Integer pageSize = 10;

}
