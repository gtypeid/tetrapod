package com.kosta.common.core.module.apiconnector;

import com.kosta.common.core.module.apiconnector.abs.RestApiConnector;
import com.kosta.common.core.module.apiconnector.data.RestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;


@Slf4j
public class WebClientConnector implements RestApiConnector {
    private final Map<String, WebClient> webClientMap;

    @Autowired
    public WebClientConnector(Map<String, WebClient> webClientMap){
        this.webClientMap = webClientMap;
        //log.warn("OLD , WEB Client LIST :: ");
        this.webClientMap.forEach( (key, value)->{
            //log.warn(key);
        });
    }

    @Override
    public <T> Mono<ResponseEntity<T>>
    asyncRequest(RestContext<?> context, Class<T> responseType) {
        String url = context.getUrl();
        String uri = context.getUri();
        WebClient webClient = webClientMap.get(url);

        Mono<ResponseEntity<T>> result = null;
        switch (context.getMethod()){
            case GET -> {
                result = webClient.get()
                        .uri(uri)
                        .retrieve()
                        .toEntity(responseType);
            }

            case POST -> {
                result = webClient.post()
                        .uri(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just( context.getBody() ), context.getBody().getClass() )
                        .exchangeToMono( clientResponse -> {
                            HttpHeaders headers = clientResponse.headers().asHttpHeaders();
                            HttpStatusCode statusCode = clientResponse.statusCode();

                            return clientResponse.bodyToMono( responseType )
                                    .map( body ->{
                                        return ResponseEntity.status(statusCode)
                                                .headers(headers)
                                                .body( body );
                                    });
                        });

                        //.retrieve()
                        //.toEntity(responseType);
            }

            default -> Mono.error(new UnsupportedOperationException("Unsupported method: " + context.getMethod()));
        }

        return result;
    }
}
