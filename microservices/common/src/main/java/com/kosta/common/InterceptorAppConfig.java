package com.kosta.common;

import com.kosta.common.core.module.apichecker.APICheckerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorAppConfig implements WebMvcConfigurer {

    @Autowired
    APICheckerInterceptor apiCheckerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiCheckerInterceptor)
                .excludePathPatterns("/api/**", "/error");
    }



}
