package com.kosta.common.core.module.apiconnector.data;


import com.kosta.common.core.data.argctx.ServerTypeContext;
import com.kosta.common.core.module.apiconnector.abs.RestApiConnector;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class RestContext<T> {
    private RestApiConnector.Method method;
    private String url;
    private String uri;
    private T body;
    //private HttpServletRequest request;
    private ServerTypeContext infoCTX;
    private Runnable onBeforeRequest;
}
