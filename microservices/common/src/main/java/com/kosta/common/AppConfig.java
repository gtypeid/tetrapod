package com.kosta.common;

import com.kosta.common.core.module.apiconnector.WebClientConnector;
import com.kosta.common.core.module.apiconnector.abs.RestApiConnector;
import com.kosta.common.core.module.generator.SequenceGenerator;
import com.kosta.common.core.module.generator.SnowFlakeGenerator;
import com.kosta.common.core.module.generator.TicketServerGenerator;
import com.kosta.common.core.module.generator.abs.IdGenerator;
import com.kosta.common.core.module.shading.SimpleSharding;
import com.kosta.common.core.module.shading.abs.ShardingMatcher;
import com.kosta.common.spring.properties.APIServerProperties;
import com.kosta.common.spring.properties.ServerProperties;
import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@PropertySource("classpath:common.properties")
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public ShardingMatcher shardingMatcher(){
        return new SimpleSharding();
    }

    @Bean
    public ServerProperties serverProperties(){
        return new APIServerProperties();
    }

    @Bean
    public Map<String, WebClient> webClientMap(ServerProperties serverProperties){
        Map<String, WebClient> webClientMap = new HashMap<>();
        String[] urls = serverProperties.getUrlList();

        ConnectionProvider connectionProvider = ConnectionProvider.builder("custom")
                .maxConnections(5000)
                .pendingAcquireMaxCount(50000)
                .maxIdleTime(Duration.ofSeconds(3)) // idle 상태의 최대 수명 시간
                .maxLifeTime(Duration.ofSeconds(29)) // Connection Pool 에서의 최대 수명 시간
                .build();

        LoopResources loopResources = LoopResources.create("event-loop", 8, true);
        HttpClient httpClient = HttpClient.create(connectionProvider)
                .runOn(loopResources)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 45000)
                .responseTimeout(Duration.ofSeconds(45));

        for(int i = 0; i < urls.length; ++i){
            webClientMap.put(urls[i],
                    WebClient.builder()
                            .baseUrl(urls[i])
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .clientConnector( new ReactorClientHttpConnector(httpClient) )
                            .build() );
        }

        return webClientMap;
    }

    @Bean()
    public RestApiConnector restApiConnector() {
        return new WebClientConnector( webClientMap( serverProperties() ) );
    }
}


// 추가 설정은 아니지만 문제 해결 키워드
/*
    @Bean
    public ReactiveWebServerFactory reactiveWebServerFactory() {
        NettyReactiveWebServerFactory factory = new NettyReactiveWebServerFactory();
        // 여기서 Netty의 다양한 설정을 커스터마이즈할 수 있습니다.
        System.out.println("-----HELLO");
        return factory;
    }
*/
    /*
    @Bean
    public NettyServerCustomizer nettyServerCustomizer() {
        return httpServer -> {
            System.out.println("---HELLO");
            LoopResources loopResources = LoopResources.create("my-http", 1, true);
            return httpServer.runOn(loopResources);
        };
    }
    */