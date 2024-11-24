package com.kosta.apiserver.spring;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.stereotype.Component;
import reactor.netty.http.server.HttpServer;

public class EventLoopNettyCustomizer implements NettyServerCustomizer {

    @Override
    public HttpServer apply(HttpServer httpServer) {
        System.out.println("HEELO@");
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(16);
        return httpServer.runOn(eventLoopGroup);
    }
}