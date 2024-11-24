package com.kosta.common.core.data.argsparser;


import com.kosta.common.core.data.argctx.ServerChainContext;
import com.kosta.common.core.data.argctx.ServerTypeContext;
import com.kosta.common.core.data.argsparser.abs.ArgsParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;


@Component
public class ServerTypeArgsParser implements ArgsParser<ServerTypeContext> {

    private final ServerProperties serverProperties;

    @Autowired
    public ServerTypeArgsParser(ServerProperties serverProperties){
        this.serverProperties = serverProperties;
    }

    @Override
    public ServerTypeContext casting(String value){
        ServerTypeContext ctx = new ServerTypeContext();

        String[] split = value.split(",");
        ctx.setServerType(split[0]);
        ctx.setServerLabel(split[1]);
        ctx.setGridX(split[2]);
        ctx.setGridY(split[3]);
        ctx.setServerPort( serverProperties.getPort() );

        try {
            InetAddress address = InetAddress.getLocalHost();
            ctx.setServerAddress(address.getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return ctx;
    }

    @Override
    public String getArgsType() {
        return "server.type";
    }
}
