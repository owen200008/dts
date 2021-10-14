package com.utopia.data.transfer.web.archetype.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.lob.DefaultLobHandler;

@Configuration
public class DBConfig {

    @Bean
    public DefaultLobHandler getDefaultLobHandler(){
        DefaultLobHandler defaultLobHandler = new DefaultLobHandler();
        defaultLobHandler.setStreamAsLob(true);
        return defaultLobHandler;
    }

}