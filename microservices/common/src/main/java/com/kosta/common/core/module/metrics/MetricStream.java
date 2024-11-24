package com.kosta.common.core.module.metrics;

import com.kosta.common.CommandLineAppStartupRunner;
import com.kosta.common.core.data.argctx.ServerTypeContext;
import com.kosta.rb.actor.metrics.dynamicflow.PtrComputer;
import com.kosta.rb.actor.metrics.dynamicflow.TrafficFlowContext;
import com.kosta.rb.actor.metrics.dynamicflow.abs.UniqueDynamicFlowContext;
import com.kosta.rb.core.FlowClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
public class MetricStream {

    @Value("${metric.server}")
    private String metricServer;

    @Value("${metric.port}")
    private String metricPort;

    private ConcurrentLinkedQueue<UniqueDynamicFlowContext> queue = new ConcurrentLinkedQueue<>();

    private final CommandLineAppStartupRunner commandLineAppStartupRunner;
    private FlowClient flowClient;

    @Autowired
    public MetricStream(CommandLineAppStartupRunner commandLineAppStartupRunner){
        this.commandLineAppStartupRunner = commandLineAppStartupRunner;
        this.commandLineAppStartupRunner.onRun = this::run;
    }

    public <T extends UniqueDynamicFlowContext>void addRequest(HttpServletRequest request, Class<T> type){
        if(flowClient == null) return;

        if(type.getName().equals(TrafficFlowContext.class.getName())){
            String clientIP = request.getRemoteAddr();
            String clientType = request.getHeader("Client-Type");
            String clientLabel = request.getHeader("Client-Label");
            String clientPort = request.getHeader("Client-Port");
            String requestHost = request.getHeader("Host");
            String serverName = request.getServerName();
            String serverPort = String.valueOf(request.getServerPort());
            String requestURL = request.getRequestURL().toString();
            String requestURI = request.getRequestURI();
            String method = request.getMethod();
            ServerTypeContext typeCtx = commandLineAppStartupRunner.getArgsConText(ServerTypeContext.class);

            if(clientType == null || clientType.isEmpty() || clientType.equals("null")){
                log.warn("CLIENT NULL / URI : {}, METHOD {} ", requestURI, request);
                return;
            }

            log.warn("* ----->  ");
            log.warn("* CLIENT IP : {}", clientIP);
            log.warn("* CLIENT TYPE  : {}", clientType);
            log.warn("* CLIENT LABEL : {}", clientLabel);
            log.warn("* CLIENT PORT  : {}", clientPort);
            log.warn("* |-----|  ");
            log.warn("* CURRENT IP : {}", serverName);
            log.warn("* CURRENT PORT : {}", serverPort);
            log.warn("* CURRENT TYPE : {}", typeCtx.getServerType());
            log.warn("* CURRENT LABEL : {}", typeCtx.getServerLabel());
            log.warn("* CURRENT URI : {}", requestURI);
            log.warn("* CURRENT METHOD : {}", method);
            log.warn("* CURRENT BODY : {}", request.getAttribute("requestBody"));
//            log.warn("CURRENT HOST : {}", requestHost);
//            log.warn("CURRENT URL : {}", requestURL);
//            log.warn("FLOW CLIENT :: {}", flowClient);

            TrafficFlowContext ctx = TrafficFlowContext.builder()
                    .startPtrComputer( new PtrComputer(clientIP, clientType, clientLabel, clientPort))
                    .endPtrComputer( new PtrComputer(serverName, typeCtx.getServerType(), typeCtx.getServerLabel(), serverPort) )
                    .uri(requestURI)
                    .method(method)
                    .body( request.getAttribute("requestBody").toString() )
                    .reqs("request")
                    .date( TrafficFlowContext.genDate() )
                    .build();
            queue.offer(ctx);

        }
    }

    public <T extends UniqueDynamicFlowContext>void addResponse(
            HttpServletRequest request,
            ContentCachingResponseWrapper response, Class<T> type){
        if(flowClient == null) return;

        if(type.getName().equals(TrafficFlowContext.class.getName())){

            String clientIP = request.getRemoteAddr();
            String clientType = request.getHeader("Client-Type");
            String clientLabel = request.getHeader("Client-Label");
            String clientPort = request.getHeader("Client-Port");
            ServerTypeContext typeCtx = commandLineAppStartupRunner.getArgsConText(ServerTypeContext.class);
            String requestHost = request.getHeader("Host");
            String serverName = request.getServerName();
            String serverPort = String.valueOf(request.getServerPort());
            String requestURL = request.getRequestURL().toString();
            String requestURI = request.getRequestURI();
            String method = request.getMethod();

//            log.warn("RESPONSE :: ");
//            log.warn("* ----->  ");
//            log.warn("* CLIENT IP : {}", clientIP);
//            log.warn("* CLIENT TYPE  : {}", clientType);
//            log.warn("* CLIENT LABEL : {}", clientLabel);
//            log.warn("* CLIENT PORT  : {}", clientPort);
//            log.warn("* |-----|  ");
//            log.warn("* CURRENT IP : {}", serverName);
//            log.warn("* CURRENT PORT : {}", serverPort);
//            log.warn("* CURRENT TYPE : {}", typeCtx.getServerType());
//            log.warn("* CURRENT LABEL : {}", typeCtx.getServerLabel());
//            log.warn("* CURRENT URI : {}", requestURI);
//            log.warn("* CURRENT METHOD : {}", method);
//            log.warn("* CURRENT BODY : {}", request.getAttribute("requestBody"));
//            log.warn("REQUEST URL : {}", requestURL);
//            log.warn("CLIENT STATUS : {}", response.getStatus());
//            log.warn("CLIENT PORT HEADER : {}", response.getHeader("Custom-Header"));

            String responseBody = null;
            try {
                responseBody = new String(response.getContentAsByteArray(), response.getCharacterEncoding());
//                log.warn("RESPONSE BODY : {}", responseBody);
//                log.warn("FLOW CLIENT :: {}", flowClient);

                TrafficFlowContext ctx = TrafficFlowContext.builder()
                        .startPtrComputer( new PtrComputer(serverName, typeCtx.getServerType(), typeCtx.getServerLabel(), serverPort) )
                        .endPtrComputer( new PtrComputer(clientIP, clientType, clientLabel, clientPort))
                        .uri(requestURI)
                        .method(method)
                        .body( responseBody )
                        .reqs("response")
                        .date( TrafficFlowContext.genDate() )
                        .build();
                queue.offer(ctx);

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public void run(){
        try {
            int port = Integer.parseInt(metricPort);
            log.warn("--- METRIC");
            log.warn("SERVER : {}", metricServer);
            log.warn("PORT : {}", port);
            if(flowClient == null) {
                flowClient = new FlowClient(metricServer, port);
                flowClient.runable( ()->{
                    ServerTypeContext argsCtx = commandLineAppStartupRunner.getArgsConText(ServerTypeContext.class);
                    log.warn("FLOW CLIENT :: {}", flowClient);
                    flowClient.insertComputer(argsCtx);

                    while (true){
                        UniqueDynamicFlowContext context = queue.poll();

                        if (context != null) {
                            flowClient.insertTraffic(context);
                        }

                        try {
                            Thread.sleep(100); // 100ms 대기
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt(); // 인터럽트 처리
                            break;
                        }
                    }
                });
            }
        }
        catch (Exception e){
            log.warn(e.getMessage());
        }
    }

}


