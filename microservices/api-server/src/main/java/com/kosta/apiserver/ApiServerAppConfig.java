package com.kosta.apiserver;

import com.kosta.apiserver.spring.EventLoopNettyCustomizer;
import com.kosta.common.spring.properties.DBServerProperties;
import com.kosta.common.spring.properties.ServerProperties;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.netty.resources.LoopResources;

//@Configuration
public class ApiServerAppConfig {

    @Bean
    public NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
        NettyReactiveWebServerFactory webServerFactory = new NettyReactiveWebServerFactory();
        webServerFactory.addServerCustomizers(eventLoopNettyCustomizer());
        System.out.println("HELLO!");
        return webServerFactory;
    }

    @Bean
    public EventLoopNettyCustomizer eventLoopNettyCustomizer() {
        return new EventLoopNettyCustomizer();
    }

    @Bean
    public NioEventLoopGroup eventLoopGroup() {
        return new NioEventLoopGroup();
    }

    @Bean
    public ServerProperties serverProperties(){
        return new DBServerProperties();
    }
}
