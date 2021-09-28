package com.utopia.data.transfer.web.archetype.config.web;

import com.google.common.collect.Lists;
import com.utopia.mdc.http.UtopiaMdcFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BaseWebConfig implements WebMvcConfigurer {

    private static final String ACTUATOR = "/actuator/**";
    private static final String VERSION = "/ms/v1/version";

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    public FilterRegistrationBean servletContextRequestLoggingFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(requestloggingfilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }

    public UtopiaMdcFilter requestloggingfilter() {
        UtopiaMdcFilter requestLoggingFilter = new UtopiaMdcFilter();
        requestLoggingFilter.setIncludeHeaders(true);
        requestLoggingFilter.setIncludeClientInfo(true);
        requestLoggingFilter.setIncludePayload(true);
        requestLoggingFilter.setIncludeQueryString(true);
        requestLoggingFilter.setMaxPayloadLength(64000);
        requestLoggingFilter.setExclusions(Lists.newArrayList(ACTUATOR, VERSION));
        return requestLoggingFilter;
    }

}