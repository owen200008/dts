package com.utopia.data.transfer.core.code.config;

import com.utopia.data.transfer.core.code.bean.DataMedia;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/9/15
 * @alter_author
 * @alter_date
 */

@Configuration
@ConfigurationProperties(prefix = "dts.data.media.desc")
public class DataMediaConfig {
    @Getter
    @Setter
    List<DataMedia> list;
}
