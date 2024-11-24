package com.kosta.apiserver.spring.ctl.async;

import com.kosta.common.core.module.apiconnector.abs.RestApiConnector;
import com.kosta.common.core.module.apiconnector.data.RestContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class BaseCRUDController<T> {

    private final RestApiConnector restApiConnector;

    @Autowired
    public BaseCRUDController(RestApiConnector restApiConnector){
        this.restApiConnector = restApiConnector;
    }

    @PostMapping("/create")
    public ResponseEntity<T> createEntity(HttpServletRequest request, @RequestBody T data){
        String uri = request.getRequestURI();
        String randURL = "test";//dbServerProperties.randURL();

        return restApiConnector.request(RestContext.builder()
                        .method(RestApiConnector.Method.POST)
                        .url(randURL)
                        .uri(uri)
                        .body(data).build()
                , getEntityClass() );
    }


    protected abstract Class<T> getEntityClass();
}
