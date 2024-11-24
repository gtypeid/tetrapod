package com.kosta.common.core.data.argsparser;


import com.kosta.common.core.data.argctx.LoadBalancingContext;
import com.kosta.common.core.data.argctx.ServerChainContext;
import com.kosta.common.core.data.argsparser.abs.ArgsParser;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;


@Component
public class LoadBalancingArgsParser implements ArgsParser<LoadBalancingContext>{

    @Override
    public LoadBalancingContext casting(String value){
        LoadBalancingContext ctx = new LoadBalancingContext();
        String[] split = value.split(",");
        ctx.setBalancingType(split[0]);
        ctx.setStatusCheckLayer(split[1]);
        return ctx;
    }

    @Override
    public String getArgsType() {
        return "loadbalancing.type";
    }
}
