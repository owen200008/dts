package com.utopia.data.transfer.admin.vo.res;

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
public class PipeParamsRes {

    private Integer id;

    private String name;

    private String pipelineParams;
}
