package com.kosta.common.core.module.apiconnector.abs;

import com.kosta.common.core.module.apiconnector.data.RestContext;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface RestApiConnector{
    enum Method {
        GET,
        POST,
        PUT,
        DELETE,
    }

    default public <T> ResponseEntity<T> request(RestContext<?> context, Class<T> responseType){
        return null;
    }

    default public <T> Mono<ResponseEntity<T>> asyncRequest(RestContext<?> context, Class<T> responseType) {
        return null;
    }
}
