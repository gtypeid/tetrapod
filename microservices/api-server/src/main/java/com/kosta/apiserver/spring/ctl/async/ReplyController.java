package com.kosta.apiserver.spring.ctl.async;

import com.kosta.common.core.module.apiconnector.abs.RestApiConnector;
import com.kosta.common.core.module.apiconnector.data.RestContext;
import com.kosta.common.spring.data.vo.Reply;
import com.kosta.common.spring.properties.ServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/reply")
@Profile("async")
public class ReplyController {

    private final RestApiConnector restApiConnector;
    private final ServerProperties serverProperties;

    @Autowired
    public ReplyController(RestApiConnector restApiConnector, ServerProperties serverProperties){
        this.restApiConnector = restApiConnector;
        this.serverProperties = serverProperties;
    }

    @PostMapping("/create")
    public ResponseEntity<Reply> createReply(@RequestBody Reply reply){
        return restApiConnector.request(RestContext.builder()
                        .method(RestApiConnector.Method.POST)
                        .url("http://localhost:8093/reply/create")
                        .body(reply).build()
                , Reply.class);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<Reply> getReply(@PathVariable("replyId") Long id){
        return restApiConnector.request(RestContext.builder()
                        .method(RestApiConnector.Method.GET)
                        .url("http://localhost:8093/reply/" + id).build()
                , Reply.class);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<Reply> updateReply(@PathVariable("replyId") Long id){
        return restApiConnector.request(RestContext.builder()
                        .method(RestApiConnector.Method.PUT)
                        .url("http://localhost:8093/reply/" + id).build()
                , Reply.class);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Boolean> deleteReply(@PathVariable("replyId") Long id){
        return restApiConnector.request(RestContext.builder()
                        .method(RestApiConnector.Method.DELETE)
                        .url("http://localhost:8093/reply/" + id).build()
                , Boolean.class);
    }
}
