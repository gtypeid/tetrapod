package com.kosta.common.core.module.chain;

import com.kosta.common.CommandLineAppStartupRunner;
import com.kosta.common.core.data.argctx.ServerChainContext;
import com.kosta.common.core.data.argctx.ServerTypeContext;
import com.kosta.common.core.data.argsparser.ServerChainArgsParser;
import com.kosta.common.core.module.apichecker.APIChecker;
import com.kosta.common.core.module.apiconnector.RestTemplateConnector;
import com.kosta.common.core.module.apiconnector.abs.RestApiConnector;
import com.kosta.common.core.module.apiconnector.data.RestContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ServerChainService {

    private final APIChecker apiChecker;
    private final ServerChainArgsParser serverChainArgsParser;
    private final RestTemplateConnector restTemplateConnector;
    private final CommandLineAppStartupRunner commandLineAppStartupRunner;
    private boolean isChain = false;


    @Autowired
    public ServerChainService(APIChecker apiChecker,
                              ServerChainArgsParser serverChainArgsParser,
                              RestTemplateConnector restTemplateConnector,
                              CommandLineAppStartupRunner commandLineAppStartupRunner){
        this.apiChecker = apiChecker;
        this.serverChainArgsParser = serverChainArgsParser;
        this.restTemplateConnector = restTemplateConnector;
        this.commandLineAppStartupRunner = commandLineAppStartupRunner;
    }

    public void inIt(String target){
        ServerChainContext ctx = serverChainArgsParser.casting(target);
        apiChecker.inItCheck(ctx, ()->{
            log.warn("Chain Complete : {}", ctx);
            isChain = true;
        });
    }

    public void unChain(String target){
        ServerChainContext ctx = serverChainArgsParser.casting(target);
        apiChecker.unChain(ctx, ()->{
            log.warn("Un Chain {}", ctx);
        });
    }

    public <T> ResponseEntity<T> gate(HttpServletRequest request, Long id){
        String validPath = apiChecker.getHealthyEndpoint(request, id);
        return gateResponse(request, validPath);
    }

    public <T> ResponseEntity<T> gate(HttpServletRequest request, Class<T> type){
        String validPath = apiChecker.getHealthyEndpoint(request, type);
        return gateResponse(request, validPath);
    }

    public <T> ResponseEntity<T> gate(HttpServletRequest request, T bodyData){
        String validPath = apiChecker.getHealthyEndpoint(request, bodyData);
        return gateResponse(request, validPath);
    }

    private <T> ResponseEntity<T> gateResponse(HttpServletRequest request, String validPath){
        if(!validPath.isEmpty()){
            String uri = request.getRequestURI();
            Object data = request.getAttribute("bodyData");
            String infoMethod = (String) request.getAttribute("method");
            Class<T> responseType = (Class<T>) request.getAttribute("responseType");
            RestApiConnector.Method method = RestApiConnector.Method.valueOf(infoMethod);
            ServerTypeContext serverTypeContext = commandLineAppStartupRunner.getArgsConText(ServerTypeContext.class);

            log.warn("ValidURI :: ");
            return restTemplateConnector.request(RestContext.builder()
                    //.request(request)
                    .infoCTX(serverTypeContext)
                    .method(method)
                    .url(validPath)
                    .uri(uri)
                    .body(data)
                    .build(), responseType);
        }
        return ResponseEntity.notFound().build();
    }

    public boolean isChain(){
        return isChain;
    }
}
