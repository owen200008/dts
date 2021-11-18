package com.utopia.data.transfer.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.URL;
import java.net.URLClassLoader;

@ComponentScan(basePackages = {"com.utopia"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if(!(contextClassLoader instanceof URLClassLoader)) {
            Thread.currentThread().setContextClassLoader(new URLClassLoader(new URL[0], contextClassLoader));
        }
        SpringApplication.run(Application.class, args);
    }
}