package com.utopia.data.transfer.admin.vo.res;

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
public class EntityRes {

    private String name;

    private String type;

    private String encode;

    private Long slaveId;

    private String url;

    private String driver;

    private String username;

    private String password;

    private String mysql;
}
