package com.kosta.apiserver.spring.ctl.async;

import com.kosta.common.core.module.apiconnector.abs.RestApiConnector;
import com.kosta.common.core.module.apiconnector.data.RestContext;
import com.kosta.common.spring.data.vo.Board;
import com.kosta.common.spring.properties.ServerProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

//@RestController
//@RequestMapping("/board")
@Profile("async")
public class BoardController{

    private final RestApiConnector restApiConnector;
    private final ServerProperties serverProperties;

    @Autowired
    public BoardController(RestApiConnector restApiConnector, ServerProperties serverProperties){
        this.restApiConnector = restApiConnector;
        this.serverProperties = serverProperties;

        // 실질적 사용 호출 전에 한 번 호출해서, 리스트를 생성해 놓음
        this.serverProperties.randURL();
    }

    @PostMapping("/create")
        public Mono<ResponseEntity<Board>> createBoard(HttpServletRequest request, @RequestBody Board board) {
        String uri = request.getRequestURI();
        String randURL = serverProperties.randURL();


        return restApiConnector.asyncRequest(RestContext.builder()
                        .method(RestApiConnector.Method.POST)
                        .url(randURL)
                        .uri(uri)
                        .body(board).build(), Board.class)
                .map( it->{

                            // 기존 값은 불변성이라 직접 수정하면 안 됨
                            // 새로운 헤더 만든 후, 값 조절해서 새로운 ResponseEntity 및 값 집어넣음

                            HttpHeaders modifiedHeaders = new HttpHeaders();
                            modifiedHeaders.addAll(it.getHeaders()); // 기존 헤더 복사
                            modifiedHeaders.remove(HttpHeaders.TRANSFER_ENCODING);
                            return   ResponseEntity
                                    .status(it.getStatusCode())
                                    .headers(modifiedHeaders)
                                    .body(it.getBody() );
                });



//        ResponseEntity<Board> result = null;
//        try {
//            result = restApiConnector.request(RestContext.builder()
//                            .method(RestApiConnector.Method.POST)
//                            .url(randURL)
//                            .uri(uri)
//                            .body(board).build()
//                    , Board.class);
//        }
//        catch (Exception e){
//            System.out.println(e.getMessage());
//            if(e.getMessage().equals("URI is not absolute")){
//                System.out.println(randURL);
//                System.out.println(uri);
//            }
//            System.out.println(Arrays.toString(e.getStackTrace()));
//        }

    }

    /*
    @GetMapping("/{boardId}")
    public ResponseEntity<Board> getBoard(HttpServletRequest request, @PathVariable("boardId") Long id){
        String uri = request.getRequestURI();
        String randURL = serverProperties.randURL();

        return restApiConnector.request(RestContext.builder()
                        .method(RestApiConnector.Method.GET)
                        .url(randURL)
                        .uri(uri).build()
                , Board.class);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<Board> updateBoard(HttpServletRequest request, @PathVariable("boardId") Long id){
        String uri = request.getRequestURI();
        String randURL = serverProperties.randURL();

        return restApiConnector.request(RestContext.builder()
                        .method(RestApiConnector.Method.PUT)
                        .url(randURL)
                        .uri(uri).build()
                , Board.class);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Boolean> deleteUser(HttpServletRequest request, @PathVariable("boardId") Long id){
        String uri = request.getRequestURI();
        String randURL = serverProperties.randURL();

        return restApiConnector.request(RestContext.builder()
                        .method(RestApiConnector.Method.DELETE)
                        .url(randURL)
                        .uri(uri).build()
                , Boolean.class);
    }
     */
}
