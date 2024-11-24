package com.kosta.common.spring.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
//@EnableConfigurationProperties(APIServerProperties.class)
@ConfigurationProperties(prefix = "api.server")
public class APIServerProperties extends ServerProperties {
    public APIServerProperties(){
    }

    private String urls;

    @Override
    protected String abstractURL() {
        return urls;
    }
}
