package com.kosta.common.core.module.apiconnector;

import com.kosta.common.core.data.argctx.ServerTypeContext;
import com.kosta.common.core.module.apiconnector.abs.RestApiConnector;
import com.kosta.common.core.module.apiconnector.data.RestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RestTemplateConnector implements RestApiConnector {

    private final RestTemplate restTemplate;

    @Autowired
    public RestTemplateConnector(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> ResponseEntity<T> request(RestContext<?> context, Class<T> responseType) {
        String endPoint = context.getUrl() + context.getUri();
        ResponseEntity<T> result = null;
        HttpEntity<?> entity = null;

        log.warn("REST TAMPLATE :: {}", context);

        ServerTypeContext infoCTX = context.getInfoCTX();
        if(infoCTX != null){
            entity = new HttpEntity<>(context.getBody(), createHeaders(infoCTX) );
        }

        if(context.getOnBeforeRequest() != null)
            context.getOnBeforeRequest().run();

        switch (context.getMethod()) {
            case GET:
                result = restTemplate.exchange(endPoint, HttpMethod.GET, entity, responseType);
                break;
            case POST:
                result = restTemplate.exchange(endPoint, HttpMethod.POST, entity, responseType);
                break;

                /*
            case PUT:
                restTemplate.put(context.getUrl(), context.getBody());
                return ResponseEntity.ok().build();  // PUT 요청은 응답이 없을 수 있으므로 적절히 처리
            case DELETE:
                restTemplate.delete(context.getUrl());
                return ResponseEntity.ok().build();  // DELETE 요청은 응답이 없을 수 있으므로 적절히 처리
                 */
            default:
                throw new UnsupportedOperationException("Unsupported method: " + context.getMethod());
        }

        HttpHeaders resultHeaders = new HttpHeaders();
        resultHeaders.addAll(result.getHeaders());
        resultHeaders.remove(HttpHeaders.TRANSFER_ENCODING);
        return new ResponseEntity<>(result.getBody(), resultHeaders, result.getStatusCode());
    }

    private HttpHeaders createHeaders(ServerTypeContext infoCTX) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-Type", infoCTX.getServerType());
        headers.set("Client-Label", infoCTX.getServerLabel());
        headers.set("Client-Port", String.valueOf(infoCTX.getServerPort()));
        return headers;
    }
}
