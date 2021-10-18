package com.utopia.data.transfer.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * describe:
 *
 * @author lxy
 * @date 2021/10/14
 */
@ComponentScan(basePackages = {"com.utopia"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@SpringBootApplication(exclude = RedisAutoConfiguration.class)
@MapperScan({"com.utopia.data.transfer.admin.dao.mapper"})
public class DtsAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(DtsAdminApplication.class, args);
    }
}
