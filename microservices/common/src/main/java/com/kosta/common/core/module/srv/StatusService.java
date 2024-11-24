package com.kosta.common.core.module.srv;

import com.kosta.common.CommandLineAppStartupRunner;
import com.kosta.common.core.data.argctx.LoadBalancingContext;
import com.kosta.common.core.data.argctx.ServerChainContext;
import com.kosta.common.core.data.argctx.ServerTypeContext;
import com.kosta.common.core.data.ctx.StatusContext;
import com.kosta.common.core.module.apiconnector.RestTemplateConnector;
import com.kosta.common.core.module.apiconnector.abs.RestApiConnector;
import com.kosta.common.core.module.apiconnector.data.RestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StatusService {

    private final RestTemplateConnector restTemplateConnector;
    private final CommandLineAppStartupRunner commandLineAppStartupRunner;

    public StatusService(RestTemplateConnector restTemplateConnector,
                         CommandLineAppStartupRunner commandLineAppStartupRunner){
        this.restTemplateConnector = restTemplateConnector;
        this.commandLineAppStartupRunner = commandLineAppStartupRunner;
    }

    public StatusContext getCurrentStatus(){
        log.warn("CALL CURRENT STATUS");
        return StatusContext.builder().status("UP").build();
    }

    public StatusContext getTargetStatus(ServerChainContext serverChainContext){

        log.warn("STATUS CHECK :: ");
        log.warn(String.valueOf(serverChainContext));

        StatusContext statusContext = null;
        LoadBalancingContext balancingContext = commandLineAppStartupRunner.getArgsConText(LoadBalancingContext.class);
        ServerTypeContext serverTypeContext = commandLineAppStartupRunner.getArgsConText(ServerTypeContext.class);
        String layer = balancingContext.getStatusCheckLayer();

        try {
            if(layer.equals("layer7")){
                statusContext = restTemplateConnector.request(RestContext.builder()
                                .infoCTX(serverTypeContext)
                                .method(RestApiConnector.Method.GET)
                                .url(serverChainContext.getPath())
                                .uri("/api/status")
                                .build(), StatusContext.class)
                        .getBody();
            }
            else {

            }
        }
        catch (Exception e){
            log.warn("STATUS ERROR");
            log.warn(String.valueOf(statusContext));
        }

        log.warn("STATUS RESPONSE ");
        log.warn(String.valueOf(statusContext));
        return statusContext;
    }
}
