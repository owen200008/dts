package com.utopia.data.transfer.admin.vo;

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
public class QueryEntityVo {

    private String name ;

    private String type = "MYSQL";

    private Integer pageNum = 1;

    private Integer pageSize = 20;
}
