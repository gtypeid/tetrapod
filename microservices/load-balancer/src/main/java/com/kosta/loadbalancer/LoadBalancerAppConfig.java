package com.kosta.loadbalancer;


import com.kosta.common.spring.properties.APIServerProperties;
import com.kosta.common.spring.properties.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LoadBalancerAppConfig {

    @Bean()
    public ServerProperties serverProperties(){
        return new APIServerProperties();
    }


}
