package com.utopia.data.transfer.core.code.config.data.media.source;

import com.utopia.data.transfer.core.code.base.datasource.bean.DataMediaSource;
import com.utopia.data.transfer.core.code.base.datasource.bean.db.DbMediaSource;
import com.utopia.data.transfer.core.code.config.DataMediaSourceConfig;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */

@Configuration
@ConfigurationProperties(prefix = "dts.data.media.source.db")
public class DataMediaSourceDB implements DataMediaSourceConfig {
    @Setter
    List<DbMediaSource> list;

    @Override
    public Map<Long, DataMediaSource> getMapDataMediaSource(){
        return list.stream().collect(Collectors.toMap(DbMediaSource::getId, item->item));
    }
}
