package com.utopia.data.transfer.core.code.config;

import com.utopia.data.transfer.core.code.base.datasource.bean.DataMediaSource;

import java.util.Map;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */

public interface DataMediaSourceConfig {

    Map<Long, DataMediaSource> getMapDataMediaSource();
}
