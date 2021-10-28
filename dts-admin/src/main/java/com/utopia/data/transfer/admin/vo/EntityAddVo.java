package com.utopia.data.transfer.admin.vo;


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
public class EntityAddVo {

    @NotNull
    private String name;
    @NotNull
    private String type;

    private String encode;

    @NotNull
    private String url;
    @NotNull
    private String driver;
    @NotNull
    private String username;
    @NotNull
    private String password;

    private String property;

}
