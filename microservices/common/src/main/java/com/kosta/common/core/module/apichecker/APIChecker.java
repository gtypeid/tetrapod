package com.kosta.common.core.module.apichecker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kosta.common.core.data.argctx.ServerChainContext;
import com.kosta.common.core.module.apichecker.data.MappingInfoAPI;
import com.kosta.common.core.module.apiconnector.RestTemplateConnector;
import com.kosta.common.core.module.apiconnector.abs.RestApiConnector;
import com.kosta.common.core.module.apiconnector.data.RestContext;
import com.kosta.common.core.module.balancer.LoadBalancer;
import com.kosta.common.core.module.chain.chainfilter.abs.ServerChainAdapter;
import com.kosta.common.core.module.generator.IdGeneratorModule;
import com.kosta.common.spring.data.dto.abs.DTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Component
public class APIChecker {

    private final RestTemplateConnector restTemplateConnector;
    private final LoadBalancer loadBalancer;
    private final IdGeneratorModule idGeneratorModule;
    private List<ServerChainAdapter> serverChainAdapterList;
    private final ObjectMapper objectMapper = new ObjectMapper();


    private List<ServerChainContext> serverChainContextList = new ArrayList<>();

    private Map<String, Map<String, List<MappingInfoAPI>>> serverChainApis = new ConcurrentHashMap<>();

    @Autowired
    public APIChecker( RestTemplateConnector restTemplateConnector,
                       LoadBalancer loadBalancer,
                       IdGeneratorModule idGeneratorModule,
                       List<ServerChainAdapter> serverChainAdapterList
                       ){
        this.restTemplateConnector = restTemplateConnector;
        this.idGeneratorModule = idGeneratorModule;
        this.loadBalancer = loadBalancer;
        this.serverChainAdapterList = serverChainAdapterList;
    }

    public void inItCheck(ServerChainContext ctx, Runnable runnable){

        log.warn("API CHECKER INIT");
        MappingInfoAPI[] result = null;
        try {
            result = restTemplateConnector.request(RestContext.builder()
                            .method(RestApiConnector.Method.GET)
                            .url(ctx.getPath())
                            .uri("/api/apis").build(),
                    MappingInfoAPI[].class).getBody();

            for (int i = 0; i < result.length; ++i){
                String method = result[i].getMethod();
                boolean hasChain = serverChainApis.containsKey(ctx.getKey());

                if(!hasChain){
                    serverChainApis.put( ctx.getKey(), new HashMap<String, List<MappingInfoAPI>>() );
                }

                Map<String, List<MappingInfoAPI>> apiStore = getApiStore(ctx);
                if( !apiStore.containsKey(method) ){
                    apiStore.put(method, new ArrayList<>() );
                }

                List<MappingInfoAPI> list = getMethodList(ctx, method);
                list.add(result[i]);
                log.warn(String.valueOf(result[i]));
            }

            serverChainContextList.add(ctx);
            runnable.run();
        }
        catch (Exception e){
            log.warn("-----------------------");
            log.warn("NOT FOUND TARGET SERVER");
            log.warn(e.getMessage());
            log.warn("-----------------------");
            // System.exit(0);
        }
    }

    public void unChain(ServerChainContext ctx, Runnable runnable){
        /*
        if( inCTX.getPath().equals(ctx.getPath()) ){
            isChain = false;
        }
        */
        runnable.run();
    }

    private List<MappingInfoAPI> getMethodList(ServerChainContext ctx, String method){
        return getApiStore(ctx).get(method);
    }

    private Map<String, List<MappingInfoAPI>> getApiStore(ServerChainContext ctx){
        return serverChainApis.get(ctx.getKey());
    }

    public <T> String getHealthyEndpoint(HttpServletRequest request, Long id){
        return getHealthyEndpointsForKey(request, null, id);
    }

    public <T> String getHealthyEndpoint(HttpServletRequest request, Class<T> type){

        T bodyData = null;
        try {
            bodyData = objectMapper.readValue(request.getAttribute("requestBody").toString(), type);
        } catch (JsonProcessingException e) {
            log.warn("BODY CASTING ERROR");
            throw new RuntimeException(e);
        }
        return getHealthyEndpoint(request, bodyData);
    }

    public <T> String getHealthyEndpoint(HttpServletRequest request, T bodyData){
        return getHealthyEndpointsForKey(request, bodyData, -1L);
    }

    private <T> String getHealthyEndpointsForKey(HttpServletRequest request, T bodyData, Long inId){

        String validPath = "";
        Long shardingKey = inId;
        if(bodyData != null){
            if( bodyData instanceof DTO<?>){
                DTO<?> bodyDto = ((DTO<?>) bodyData);
                Long dtoId = bodyDto.getId();
                shardingKey = idGeneratorModule.getGenerateId(dtoId);
                bodyDto.setId(shardingKey);
                log.warn("DTO : {}", bodyData);
                log.warn("DTO ID KEY : {}", shardingKey);
            } else {
                log.warn("NOT DTO : {}", bodyData);
            }
        }

        List<ServerChainContext> chainAdapters = matchChainAdapter(request, bodyData, serverChainContextList);
        ServerChainContext activeCtx = loadBalancer.assessContext(chainAdapters, shardingKey);
        MappingInfoAPI mappingInfoAPI = getValidApi(request, activeCtx);
        if ( validBody(mappingInfoAPI, request, bodyData) ){
            validPath = activeCtx.getPath();
        }
        return validPath;
    }

    private <T> List<ServerChainContext> matchChainAdapter(
            HttpServletRequest request,
            T bodyData,
            List<ServerChainContext> chainContextList){

        log.warn("SERVER CHAIN ADAPTERS ");
        List<ServerChainContext> matchServerChainList = new ArrayList<>();

        for (ServerChainContext chainCtx : chainContextList){
            Optional<ServerChainAdapter> optionalAdapter = serverChainAdapterList.stream().filter(chainAdapter->{
                return chainAdapter.getClass()
                        .getSimpleName()
                        .equals(chainCtx.getChainAdapter());
            }).findAny();

            if (optionalAdapter.isPresent()) {
                ServerChainAdapter matchAdapter = optionalAdapter.get();
                if( matchAdapter.match(request, chainCtx) ){
                    List<ServerChainContext> matchAdapters = new ArrayList<>();
                    matchAdapters.add( chainCtx );
                    log.warn("FOUND ADAPTER {}", matchAdapters);
                    return matchAdapters;
                }
            }
            else {
                matchServerChainList.add(chainCtx);
            }
        }

        log.warn("NOT FOUND ADAPTER {}", matchServerChainList);
        return matchServerChainList;
    }

    private <T> MappingInfoAPI getValidApi(HttpServletRequest request, ServerChainContext activeCtx){

        Map<String, List<MappingInfoAPI>> apiStore = getApiStore(activeCtx);
        MappingInfoAPI mappingInfoAPI = null;
        String url = request.getRequestURI();
        String method = request.getMethod();

        if ( apiStore.containsKey(method) ){
            PathPatternParser parser = new PathPatternParser();
            List<MappingInfoAPI> list = apiStore.get(method);
            MappingInfoAPI info = list.stream().filter( it -> {
                        return parser.parse(it.getPath())
                                .matches( PathContainer.parsePath(url) );
                    }
            ).findAny().get();
            if( info != null){
                mappingInfoAPI = info;
            }

        }
        return mappingInfoAPI;
    }

    private <T> boolean validBody(MappingInfoAPI infoAPI, HttpServletRequest request, T bodyData){
        boolean flag = false;

        log.warn("Mapping API :{}", infoAPI);
        log.warn("Request :{}", request);

        if ( infoAPI.getMethod().equals("POST") ||
                infoAPI.getMethod().equals("PUT") ){

            Class<?> returnTypeClass = null;
            Object bodyObject = null;
            try {
                returnTypeClass = Class.forName(infoAPI.getReturnType());
                bodyObject = bodyData;

                log.warn("API Return Type :{}", returnTypeClass.getName());
                log.warn("Body Object Type :{}", bodyObject.getClass().getName());
                log.warn("Body Object :{}", bodyObject);

                if(returnTypeClass.equals(bodyObject.getClass())){
                    flag = true;
                    request.setAttribute("bodyData", bodyObject);
                    request.setAttribute("method", infoAPI.getMethod());
                    request.setAttribute("responseType", returnTypeClass);
                }

            } catch (Exception e) {
            }
        }
        else {
            flag = true;
            try {
                Class<?> returnTypeClass = Class.forName(infoAPI.getReturnType());
                request.setAttribute("method", infoAPI.getMethod());
                request.setAttribute("responseType", returnTypeClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return flag;
    }
}