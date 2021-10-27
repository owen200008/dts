package com.utopia.data.transfer.web.archetype.config.web;

import com.utopia.register.center.api.ZookeeperConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.lob.DefaultLobHandler;

@Configuration
@EnableConfigurationProperties({ZookeeperConfig.class, KafkaProperties.class})
public class DBConfig {

    @Bean
    public DefaultLobHandler getDefaultLobHandler(){
        DefaultLobHandler defaultLobHandler = new DefaultLobHandler();
        defaultLobHandler.setStreamAsLob(true);
        return defaultLobHandler;
    }

}