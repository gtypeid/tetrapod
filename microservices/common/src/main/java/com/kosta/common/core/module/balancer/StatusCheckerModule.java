package com.kosta.common.core.module.balancer;

import com.kosta.common.core.data.argctx.LoadBalancingContext;
import com.kosta.common.core.data.argctx.ServerChainContext;
import com.kosta.common.core.data.ctx.StatusContext;
import com.kosta.common.core.module.srv.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusCheckerModule {

    private static final Logger log = LoggerFactory.getLogger(StatusCheckerModule.class);
    private final StatusService statusService;

    @Autowired
    public StatusCheckerModule(StatusService statusService){
        this.statusService = statusService;
    }

    public StatusContext statCheck(LoadBalancingContext balancingCtx, ServerChainContext chainCtx){
        String layer = balancingCtx.getStatusCheckLayer();
        StatusContext statusCtx = null;
        log.warn("LAYER : {}", layer);
        if(layer.equals("none") || layer.isEmpty()){
            statusCtx = StatusContext.builder().status("UP").build();
        }
        else if(layer.equals("layer4") || layer.equals("layer7")){
            statusCtx = statusService.getTargetStatus(chainCtx);
        }

        return statusCtx;
    }

}
