package com.utopia.data.transfer.admin.vo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PipelineAddVo {

    @NotNull
    private  String name;
    @NotNull
    private Integer taskId;
    @NotNull
    private Integer sourceEntityId;
    @NotNull
    private Integer targetEntityId;
    @NotNull
    private String pipelineParams;
}
