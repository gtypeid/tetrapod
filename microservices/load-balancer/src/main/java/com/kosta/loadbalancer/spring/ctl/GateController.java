package com.kosta.loadbalancer.spring.ctl;


import com.kosta.common.core.module.apiconnector.data.RestContext;
import com.kosta.common.core.module.apiconnector.abs.RestApiConnector;
import com.kosta.common.spring.properties.ServerProperties;
import com.kosta.common.core.module.apichecker.APIChecker;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/**")
public class GateController {
    private final APIChecker apiChecker;
    private final RestApiConnector restApiConnector;
    private final ServerProperties serverProperties;

    @Autowired
    public GateController(APIChecker apiChecker, RestApiConnector restApiConnector, ServerProperties serverProperties){
        this.apiChecker = apiChecker;
        this.restApiConnector = restApiConnector;
        this.serverProperties = serverProperties;
    }

    @RequestMapping()
    public <T> Flux< ResponseEntity<T>> fluxGate(){
        return null;
    }

    @RequestMapping()
    public <T> Mono< ResponseEntity<T>> gate(HttpServletRequest request) throws IOException {

        return null;
        /*
        if(apiChecker.getHealthyEndpoint(request).isEmpty())
            return Mono.just( ResponseEntity.badRequest().build() );

        String uri = request.getRequestURI();
        String randURL = serverProperties.randURL();
        Object data = request.getAttribute("bodyData");
        String infoMethod = (String) request.getAttribute("method");
        Class<T> responseType = (Class<T>) request.getAttribute("responseType");
        RestApiConnector.Method method = RestApiConnector.Method.valueOf(infoMethod);

        //System.out.println("REQUEST : " + method + " " + randURL + uri + "\n" + data);
        return restApiConnector.asyncRequest(RestContext.builder()
                        .method(method)
                        .url(randURL)
                        .uri(uri)
                        .body(data).build(), responseType)
                        .map( it->{

                            // 기존 값은 불변성이라 직접 수정하면 안 됨
                            // 새로운 헤더 만든 후, 값 조절해서 새로운 ResponseEntity 및 값 집어넣음

                            HttpHeaders modifiedHeaders = new HttpHeaders();
                            modifiedHeaders.addAll(it.getHeaders()); // 기존 헤더 복사
                            modifiedHeaders.remove(HttpHeaders.TRANSFER_ENCODING);
                            return   ResponseEntity
                                    .status(it.getStatusCode())
                                    .headers(modifiedHeaders)
                                    .body(it.getBody());
                        }).doOnNext( it ->{
                            //System.out.println("1");
                            //System.out.println(it);
                });

         */

    }

}
