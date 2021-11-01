package com.utopia.data.transfer.model.code.entity.mysql;

import com.utopia.data.transfer.model.code.entity.CanalSupport;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author owen.cai
 * @create_date 2021/10/13
 * @alter_author
 * @alter_date
 */
public class MysqlProperty implements CanalSupport, Serializable {

    /**
     * db的配置
     */
    @Getter
    @Setter
    private String                                      url = "jdbc:mysql://xxx.xxx.xxx:3306?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
    @Getter
    @Setter
    private String                                      username = "root";
    @Getter
    @Setter
    private String                                      password = "root";
    @Getter
    @Setter
    private String                                      driver = "com.mysql.cj.jdbc.Driver";
    /**
     * 编码方式
     */
    @Getter
    @Setter
    private String                                      encode;

    @Getter
    @Setter
    private int                                         maxWait                       = 60 * 1000;
    @Getter
    @Setter
    private int                                         minIdle                       = 0;
    @Getter
    @Setter
    private int                                         initialSize                   = 0;
    @Getter
    @Setter
    private int                                         maxActive                     = 32;
    @Getter
    @Setter
    private int                                         maxIdle                       = 32;
    @Getter
    @Setter
    private int                                         numTestsPerEvictionRun        = -1;
    @Getter
    @Setter
    private int                                         timeBetweenEvictionRunsMillis = 60 * 1000;
    @Getter
    @Setter
    private int                                         removeAbandonedTimeout        = 5 * 60;
    @Getter
    @Setter
    private int                                         minEvictableIdleTimeMillis    = 5 * 60 * 1000;
}
