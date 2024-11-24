package com.kosta.common.spring.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "db.server")
public class DBServerProperties extends ServerProperties{
    public DBServerProperties(){
    }

    private String urls;

    @Override
    protected String abstractURL() {
        return urls;
    }
}
