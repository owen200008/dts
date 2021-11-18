package com.utopia.data.transfer.core.code.config;

import com.utopia.data.transfer.core.base.config.DTSConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/11/17
 * @alter_author
 * @alter_date
 */
@Configuration
@ConfigurationProperties(prefix = DTSConstants.CONF_PATH_JAR)
@Order(-1)
@Data
public class JarConfig {
    private String path;
    private List<String> jars;
}
