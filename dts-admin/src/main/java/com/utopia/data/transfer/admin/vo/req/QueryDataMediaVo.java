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
public class QueryDataMediaVo {

    private String name;

    private Integer pageNum;

    private  Integer pageSize;
}
