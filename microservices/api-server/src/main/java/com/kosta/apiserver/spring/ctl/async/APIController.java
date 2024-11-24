package com.kosta.apiserver.spring.ctl.async;

import com.kosta.common.spring.data.vo.Board;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;
import java.time.Instant;

//@RestController
@Profile("async")
public class APIController {
    private static final AtomicLong SEQ = new AtomicLong(0);

    private final NioEventLoopGroup eventLoopGroup;
    //private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    private WebClient webClient;
    @Autowired
    public APIController(NioEventLoopGroup eventLoopGroup){
        this.eventLoopGroup = eventLoopGroup;


        ConnectionProvider connectionProvider = ConnectionProvider.builder("myConnectionPool")
                .maxConnections(10000)  // 최대 커넥션 수를 늘립니다.
                .pendingAcquireMaxCount(50000)  // 대기열 크기도 증가시킵니다.
                .build();

        ReactorClientHttpConnector clientHttpConnector = new ReactorClientHttpConnector(HttpClient.create(connectionProvider));

//        HttpClient httpClient = HttpClient.create(connectionProvider)
//                .responseTimeout(Duration.ofSeconds(10))  // 응답 타임아웃 설정
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)  // 연결 타임아웃 설정
//                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10))  // 읽기 타임아웃 설정
//                        .addHandlerLast(new WriteTimeoutHandler(10)));  // 쓰기 타임아웃 설정

        webClient = WebClient.builder()
                .baseUrl("http://localhost:8082")
                .clientConnector(clientHttpConnector)
                .build();
    }

    @GetMapping("/api-t")
    public void apit(){
        Mono.just(SEQ.incrementAndGet())
                .subscribe(System.out::println);

    }
    @PostMapping("/echo")
    public Mono<Board> echo(Board board){
        return Mono.just(board)
                .map(itBoard -> {
                    itBoard.setId(SEQ.incrementAndGet());
                    return itBoard;
                })
                .doOnNext(it -> {
                    System.out.println(it);
                });

                // flatMap은 동기임, defer로 해야함
                /*
                // 처리 될 때 Mono를 감싼 Mono<Mono>가 넘어오니 depth 삭제
                .flatMap(itBoard -> {
                    // 비동기로 처리될 callble box 할당
                    // 원래 foreach라면 동기적으로 발생함 블록 됨
                    Mono<Board> result = Mono.fromCallable( ()->{
                        eventLoopGroup.forEach(event -> {
                            NioEventLoop nioEventLoop = (NioEventLoop) event;
                            String threadName = nioEventLoop.threadProperties().name();
                            String threadState = nioEventLoop.threadProperties().state().toString();
                            System.out.println("Thread Name: " + threadName + ", State: " + threadState);
                        });

                        // 어차피 mono로 던져야하니 받아온 것 그대로 넘김
                        return itBoard;
                    });

                    // callable result return
                    return result;
                })
                */

    }

    @GetMapping("/call-sync/third-party-api")
    public ResponseEntity<String> callSyncThirdPartyAPI(){
        StringBuilder stringBuilder = new StringBuilder();
        Instant start = Instant.now();
        stringBuilder.append("Controller Call: ").append(start)
                .append("\n");


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.getForEntity("http://localhost:8082/third-party-api", String.class);
        stringBuilder
                .append("-------------------------\n")
                .append(result.getBody())
                .append("-------------------------\n");

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);

        stringBuilder.append("Controller Response: ").append(end)
                .append("\n")
                .append("Duration in seconds: ")
                .append(duration.getSeconds())
                .append("\n")
                .append("Duration in milliseconds: ")
                .append(duration.toMillis())
                .append("\n");

        System.out.println("CALLABLE : " + SEQ.incrementAndGet());
        return ResponseEntity.ok(stringBuilder.toString());
    }

    @GetMapping("/call-async/third-party-api")
    public Mono<ResponseEntity<String>> callAsyncThirdPartyAPI(){
        Instant start = Instant.now();
        return webClient.get().uri("/third-party-api")
                .retrieve()
                .toEntity(String.class)
                .map( it->{
                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append("Controller Call: ").append(start)
                            .append("\n");

                    stringBuilder
                            .append("-------------------------\n")
                            .append(it.getBody())
                            .append("-------------------------\n");

                    Instant end = Instant.now();
                    Duration duration = Duration.between(start, end);

                    stringBuilder.append("Controller Response: ").append(end)
                            .append("\n")
                            .append("Duration in seconds: ")
                            .append(duration.getSeconds())
                            .append("\n")
                            .append("Duration in milliseconds: ")
                            .append(duration.toMillis())
                            .append("\n");

                    System.out.println("CALLABLE : " + SEQ.incrementAndGet());
                    return ResponseEntity.ok( stringBuilder.toString() );
                })
                .doOnSubscribe(subscription -> System.out.println("Request sent"))
                .doOnError(error -> {
                    System.err.println("Request failed with error: " + error.getMessage());
                });
    }
    @GetMapping("/third-party-api")
    public Mono<ResponseEntity<String>> thirdPartyAPI() {
        return Mono.defer(() -> {
            Instant start = Instant.now();
            return Mono.just("Third Party API Call: " + start)
                    .delayElement(Duration.ofSeconds(1))
                    .map(result -> {
                        Instant end = Instant.now();
                        Duration duration = Duration.between(start, end);
                        return ResponseEntity.ok(result + "\n" +
                                "Third Party API End: " + end + "\n" +
                                "Duration in seconds: " + duration.getSeconds() + "\n" +
                                "Duration in milliseconds: " + duration.toMillis());
                    });
        }).doOnError(error -> {
            System.err.println("Request failed with error: " + error.getMessage());
        });
    }

    /*
    @GetMapping("/third-party-api")
    public Mono<ResponseEntity<String>> thirdPartyAPI(){
        return Mono.fromCallable( ()->{
            StringBuilder stringBuilder = new StringBuilder();
            Instant start = Instant.now();
            stringBuilder.append("   Third Party API Call : ").append(start).append("\n");

            try {
                Thread.sleep(1000);
            }
            catch (Exception e){

            };
            Instant end = Instant.now();
            stringBuilder.append("   Third Party API End : ").append(end).append("\n");

            Duration duration = Duration.between(start, end);
            stringBuilder.append("   Duration in seconds: ").append(duration.getSeconds()).append("\n");
            stringBuilder.append("   Duration in milliseconds: ").append(duration.toMillis()).append("\n");

            return ResponseEntity.ok(stringBuilder.toString());
        }).doOnError(error -> {
            System.err.println("Request failed with error: " + error.getMessage());
        });
    }
     */

//    @GetMapping("/api")
//    public List<MappingInfoAPI> getAPI(){
//        Map<RequestMappingInfo, HandlerMethod> methods = requestMappingHandlerMapping.getHandlerMethods();
//        List<MappingInfoAPI> mappingInfoAPIS = new ArrayList<>();
//        methods.forEach( (key, value)->{
//            var requestMethod = key.getMethodsCondition().getMethods()
//                    .stream().findFirst();
//
//            if(requestMethod.isPresent()){
//                String method = requestMethod.get().toString();
//                String pattern = key.getPathPatternsCondition().getPatterns()
//                        .stream().findFirst().get().toString();
//
//                Method getMethod = value.getMethod();
//                String name = getMethod.getName();
//                String returnType = getReturnType(getMethod);
//                List<String> params = Arrays.stream(getMethod.getParameterTypes())
//                        .map(Class::getName).toList();
//                mappingInfoAPIS.enqueue(new MappingInfoAPI(method, pattern, name, returnType, params) );
//            }
//        });
//        return mappingInfoAPIS;
//    }
//
//    private String getReturnType(Method method) {
//        Type genericReturnType = method.getGenericReturnType();
//        if (genericReturnType instanceof ParameterizedType) {
//            ParameterizedType monoGeneric = (ParameterizedType) genericReturnType;
//            for (Type type : monoGeneric.getActualTypeArguments()){
//                if(type instanceof  ParameterizedType){
//                    ParameterizedType generic = (ParameterizedType) type;
//                    Type[] entityGeneric = generic.getActualTypeArguments();
//                    for(Type it : entityGeneric){
//                        return it.getTypeName();
//                    }
//                }
//            }
//            return monoGeneric.getActualTypeArguments()[0].getTypeName();
//
//        } else {
//            return genericReturnType.getTypeName();
//        }
//    }
}
