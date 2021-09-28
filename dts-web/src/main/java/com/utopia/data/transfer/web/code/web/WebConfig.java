package com.utopia.data.transfer.web.code.web;

import java.util.List;
import java.util.concurrent.Executor;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 创建执行器和线程池相关，请调用这个封装下，保证上下文可以传递
     * @param executor
     * @return
     */
    Executor createUtopiaExecutor(Executor executor){
        return TtlExecutors.getTtlExecutor(executor);
    }
}