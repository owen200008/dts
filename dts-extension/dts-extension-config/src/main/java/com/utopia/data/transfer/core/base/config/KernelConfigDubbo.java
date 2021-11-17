package com.utopia.data.transfer.core.base.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ConfigurationProperties(prefix = DTSConstants.CONF_PATH_DUBBO)
@Order(-1)
public class KernelConfigDubbo {

    @Getter
    @Setter
    String register;

    @Getter
    @Setter
    String center;
}
