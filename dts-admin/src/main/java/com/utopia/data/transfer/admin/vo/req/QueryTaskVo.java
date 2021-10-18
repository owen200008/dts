package com.utopia.data.transfer.admin.vo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryTaskVo {

    private String name;

    private Integer pageSize = 1;

    private Integer pageNum = 10;
}
