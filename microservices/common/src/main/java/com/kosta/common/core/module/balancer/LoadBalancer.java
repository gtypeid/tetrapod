package com.kosta.common.core.module.balancer;

import com.kosta.common.CommandLineAppStartupRunner;
import com.kosta.common.core.data.argctx.LoadBalancingContext;
import com.kosta.common.core.data.argctx.ServerChainContext;
import com.kosta.common.core.data.ctx.StatusContext;
import com.kosta.common.core.module.shading.abs.ShardingMatcher;
import com.kosta.rb.def.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class LoadBalancer {
    private AtomicInteger SEQ = new AtomicInteger(-1);
    private LoadBalancingContext loadBalancingContext;
    private final int TRY = 3;

    private final CommandLineAppStartupRunner commandLineAppStartupRunner;
    private final StatusCheckerModule statusCheckerModule;
    private final ShardingMatcher shardingMatcher;

    @Autowired
    public LoadBalancer(CommandLineAppStartupRunner commandLineAppStartupRunner,
                        StatusCheckerModule statusCheckerModule,
                        ShardingMatcher shardingMatcher
    ){
        this.commandLineAppStartupRunner = commandLineAppStartupRunner;
        this.statusCheckerModule = statusCheckerModule;
        this.shardingMatcher = shardingMatcher;
    }

    public <T> ServerChainContext assessContext(List<ServerChainContext> chainContexts, Long shardingKey){
        ServerChainContext chainCtx = null;
        LoadBalancingContext balancingContext = getCTX();
        for(int i = 0; i < TRY; ++i){
            ServerChainContext curChainCtx = nextChainContext(balancingContext, chainContexts, shardingKey);
            StatusContext statusCtx = statusCheckerModule.statCheck(balancingContext, curChainCtx);
            if(statusCtx != null && statusCtx.getStatus().equals("UP")){
                chainCtx = curChainCtx;
                break;
            }
        }
        return chainCtx;
    }

    public LoadBalancingContext getCTX(){
        if(loadBalancingContext == null){
            loadBalancingContext = commandLineAppStartupRunner.getArgsConText(LoadBalancingContext.class);
            if(loadBalancingContext == null){
                loadBalancingContext = new LoadBalancingContext();
                loadBalancingContext.setBalancingType("random");
                loadBalancingContext.setStatusCheckLayer("none");
            }
        }
        return loadBalancingContext;
    }

    private ServerChainContext nextChainContext(LoadBalancingContext balancingContext, List<ServerChainContext> chainContexts, Long shardingKey){
        if(chainContexts.size() == 1){
            return chainContexts.get(0);
        } else {
            return assessChainContext(balancingContext, chainContexts, shardingKey);
        }
    }

    private ServerChainContext assessChainContext(LoadBalancingContext balancingContext, List<ServerChainContext> chainContexts, Long shardingKey){
        String balancingType = balancingContext.getBalancingType();
        ServerChainContext chainCtx = null;

        if(balancingType.equals("none") || balancingType.isEmpty()){
            chainCtx = chainContexts.get(0);
        }
        else if(balancingType.equals("random")){
            chainCtx = randomBalancing(chainContexts);
        }
        else if(balancingType.equals("roundrobin")){
            chainCtx = roundRobinBalancing(chainContexts);
        }
        else if(balancingType.equals("sharding")){
            chainCtx = shardBalancing(chainContexts, shardingKey);
        }

        return chainCtx;
    }

    private ServerChainContext randomBalancing(List<ServerChainContext> chainContexts){
        int size = chainContexts.size();
        int rand = Util.getRandomInt(0, size-1);
        return chainContexts.get(rand);
    }

    private ServerChainContext roundRobinBalancing(List<ServerChainContext> chainContexts){
        int next = nextBalancingSEQ(chainContexts);
        return chainContexts.get(next);
    }

    private ServerChainContext shardBalancing(List<ServerChainContext> chainContexts, Long shardingKey){
        int index = shardingMatcher.getShardingIndex(shardingKey);
        log.warn("SHARDING KEY :: " + index);
        return chainContexts.get(index);
    }

    private int nextBalancingSEQ(List<ServerChainContext> chainContexts){
        int next = SEQ.incrementAndGet();
        if(chainContexts.size() <= next){
            SEQ.set(0);
            next = 0;
        }
        return next;
    }
}
