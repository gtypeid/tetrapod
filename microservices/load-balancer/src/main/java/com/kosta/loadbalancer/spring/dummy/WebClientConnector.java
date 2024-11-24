package com.kosta.loadbalancer.spring.dummy;

import com.kosta.common.core.module.apiconnector.data.RestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;


@Component
public class WebClientConnector {
    private final Map<String, WebClient> webClientMap;

    @Autowired
    public WebClientConnector(Map<String, WebClient> webClientMap){
        this.webClientMap = webClientMap;
        System.out.println("::WEB clients");
        this.webClientMap.forEach( (key, value)->{
            System.out.println(key);
            System.out.println(value);
        });
    }

    public <T> Mono<ResponseEntity<T>> request(RestContext<?> context, Class<T> responseType) {
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
                        .retrieve()
                        .toEntity(responseType);
            }

            default -> Mono.error(new UnsupportedOperationException("Unsupported method: " + context.getMethod()));
        }

        return result;
    }
}
