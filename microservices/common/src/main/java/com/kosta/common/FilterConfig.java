package com.kosta.common;


import com.kosta.common.core.module.apichecker.APIFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<APIFilter> loggingFilter(APIFilter apiFilter){
        FilterRegistrationBean<APIFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(apiFilter);
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }
}