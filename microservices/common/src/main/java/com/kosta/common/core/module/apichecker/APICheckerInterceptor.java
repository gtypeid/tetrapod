package com.kosta.common.core.module.apichecker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.common.core.module.metrics.MetricStream;
import com.kosta.common.core.module.chain.ServerChainService;
import com.kosta.rb.actor.metrics.dynamicflow.TrafficFlowContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class APICheckerInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MetricStream metricStream;
    private final ServerChainService serverChainService;

    @Autowired
    public APICheckerInterceptor(MetricStream metricStream, ServerChainService serverChainService){
        this.metricStream = metricStream;
        this.serverChainService = serverChainService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        metricStream.addRequest(request, TrafficFlowContext.class);
        if ( !request.getRequestURI().startsWith("/api") ){
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        // 필터들어오지 않은 예를들어 api 같은것은 필터 안들어온채로 인터셉터 들어와서
        // 일반 httpResponse로 되어서 캐스팅 발생 에러 터지는듯
        if(response instanceof ContentCachingResponseWrapper ){
            ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper)response;
            if(wrapper != null){
                metricStream.addResponse(request, wrapper, TrafficFlowContext.class);
            }
        }

        if ( !request.getRequestURI().startsWith("/api") ){
        };

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }


}
