package com.kosta.common.spring.ctr;

import com.kosta.common.core.module.metrics.MetricStream;
import com.kosta.common.core.module.chain.ServerChainService;
import com.kosta.rb.actor.metrics.dynamicflow.TrafficFlowContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@Profile("sync")
@RequestMapping("/api")
public class ServerChainController {

    private final MetricStream metricStream;
    private final ServerChainService serverChainService;

    @Autowired
    public ServerChainController( MetricStream metricStream,
            ServerChainService serverChainService){
        this.metricStream = metricStream;
        this.serverChainService = serverChainService;
    };

    @GetMapping("/chain/{target}")
    public void serverChain(@PathVariable("target") String target){
        serverChainService.inIt(target);
    }

    @GetMapping("/un-chain/{target}")
    public void serverUnChain(@PathVariable("target") String target){
        serverChainService.unChain(target);
    }

    @PostMapping("/test")
    public void chainTest(HttpServletRequest servletRequest){
        metricStream.addRequest(servletRequest, TrafficFlowContext.class);
    }
}
