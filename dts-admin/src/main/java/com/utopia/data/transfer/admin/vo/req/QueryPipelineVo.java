package com.utopia.data.transfer.admin.vo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryPipelineVo {


    private Long taskId;

    private Integer pageSize = 10;

    private Integer pageNum = 1;
}
