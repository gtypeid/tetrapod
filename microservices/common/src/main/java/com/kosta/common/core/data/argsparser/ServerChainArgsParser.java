package com.kosta.common.core.data.argsparser;


import com.kosta.common.core.data.argctx.ServerChainContext;
import com.kosta.common.core.data.argsparser.abs.ArgsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;


@Component
public class ServerChainArgsParser implements ArgsParser<ServerChainContext> {

    @Override
    public ServerChainContext casting(String value){
        ServerChainContext ctx = new ServerChainContext();
        try {
            URI uri = null;
            int findIndex = value.indexOf(",");
            if(findIndex == -1){
                uri = new URI("http://" + value);
                ctx.setChainAdapter("none");
            }
            else {
                String[] split = value.split(",");
                uri = new URI("http://" + split[0]);
                ctx.setChainAdapter(split[1]);
            }

            ctx.setPath(uri.toString());
            ctx.setUrl(uri.getHost());
            ctx.setPort(uri.getPort());
            ctx.setKey( uri.getHost() + ":" + uri.getPort() );

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return ctx;
    }

    @Override
    public String getArgsType() {
        return "serverchain.target";
    }
}
