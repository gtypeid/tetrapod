package com.kosta.controlserver;

import com.kosta.controlserver.spring.handle.Cmd;
import com.kosta.controlserver.spring.handle.CmdRepository;
import com.kosta.controlserver.spring.handle.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSocket
public class ControlServerAppConfig implements WebSocketConfigurer {

    @Bean
    public CmdRepository cmdRepository(){
        return new CmdRepository();
    }

    @Bean
    public Cmd cmd(){
        return new Cmd();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketHandler( cmd() ), "api/connect")
                .setAllowedOrigins("*");
    }

}