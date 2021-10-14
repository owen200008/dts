package com.utopia.data.transfer.core.code.canal;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author owen.cai
 * @create_date 2021/10/14
 * @alter_author
 * @alter_date
 */
@Configuration
@ConfigurationProperties(prefix = "dts.canal.zk")
public class CanalZKConfig {
    @Getter
    @Setter
    private Long zkClusterId = 1L;

    @Getter
    @Setter
    private List<String> zkAddress;
}
