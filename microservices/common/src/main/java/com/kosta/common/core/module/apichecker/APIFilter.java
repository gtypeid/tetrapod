package com.kosta.common.core.module.apichecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.common.core.module.chain.ServerChainService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.IOException;

@Slf4j
@Component
public class APIFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    ServerChainService serverChainService;

    @Autowired
    public APIFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getRequestURI().startsWith("/api")) {
            chain.doFilter(request, response);
            return;
        }

        log.warn("Filter IN ::");

        RequestBodyWrapper wrapper =
                new RequestBodyWrapper((HttpServletRequest) request);

        wrapper.setAttribute("requestBody", wrapper.getRequestBody());

        //System.out.println(wrapper.getRequestBody());
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper(
                (HttpServletResponse)response);

        chain.doFilter(wrapper, httpServletResponse);
        httpServletResponse.copyBodyToResponse();

        /*
        if(serverChainService.isChain()){
            System.out.println("IS CHAIN :: ");
            ResponseEntity<?> entity = serverChainService.gate(httpRequest, null);
            System.out.println( httpResponse.isCommitted() );

            System.out.println( httpResponse.isCommitted() );
            httpResponse.setStatus(entity.getStatusCodeValue());

            HttpHeaders headers = entity.getHeaders();
            String contentType = (headers != null && headers.getContentType() != null)
                    ? headers.getContentType().toString()
                    : "application/json";
            String contentType = "application/json";
            httpResponse.setContentType(contentType);

            try (PrintWriter writer = httpResponse.getWriter()) {
                String jsonResponse = objectMapper.writeValueAsString(entity.getBody());
                writer.write(jsonResponse);
            }
        }
        */

    }

}