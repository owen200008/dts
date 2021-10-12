package com.utopia.data.transfer.core.code.base.datasource.bean.db.mysql;

import com.utopia.data.transfer.model.code.data.media.DataMediaSource;
import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

/**
 * @author owen.cai
 * @create_date 2021/9/16
 * @alter_author
 * @alter_date
 */
public class MysqlMediaSource extends DataMediaSource {
    @Getter
    @Setter
    private Properties          properties;

    @Getter
    @Setter
    private int                                       maxWait                       = 60 * 1000;
    @Getter
    @Setter
    private int                                       minIdle                       = 0;
    @Getter
    @Setter
    private int                                       initialSize                   = 0;
    @Getter
    @Setter
    private int                                       maxActive                     = 32;
    @Getter
    @Setter
    private int                                       maxIdle                       = 32;
    @Getter
    @Setter
    private int                                       numTestsPerEvictionRun        = -1;
    @Getter
    @Setter
    private int                                       timeBetweenEvictionRunsMillis = 60 * 1000;
    @Getter
    @Setter
    private int                                       removeAbandonedTimeout        = 5 * 60;
    @Getter
    @Setter
    private int                                       minEvictableIdleTimeMillis    = 5 * 60 * 1000;
}
