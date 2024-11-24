package com.kosta.databaseserver.spring.repo.postgr;

import com.kosta.common.spring.data.vo.Reply;
import com.kosta.databaseserver.spring.repo.abs.async.AsyncReplyRepository;
import com.kosta.databaseserver.spring.repo.abs.postgr.PostgrReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

public class PostgrReplyRepository implements AsyncReplyRepository {
    private static final AtomicLong SEQ = new AtomicLong(0);

    @Autowired
    private PostgrReply postgrReply;

    public Mono<ResponseEntity<Reply>> create(Reply data) {
        Reply newReply = new Reply().copy(data);
        //newReply.setTestSeq( SEQ.incrementAndGet() );

        return postgrReply.save(newReply).map(ResponseEntity::ok);
    }


    @Override
    public Mono<ResponseEntity<Reply>> read(Long id) {
        return postgrReply.findById(id).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Reply>> update(Long id, Reply data) {
        return postgrReply.findById(id).map( it -> ResponseEntity.ok( it.copy(data) ));
    }

    @Override
    public Mono<ResponseEntity<Boolean>> delete(Long id) {
        return postgrReply.deleteById(id).map( it-> ResponseEntity.ok( true ) );
    }
}
