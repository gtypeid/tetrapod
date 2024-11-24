package com.kosta.databaseserver.spring.repo.postgr;

import com.kosta.common.spring.data.vo.Board;
import com.kosta.databaseserver.spring.repo.abs.async.AsyncBoardRepository;
import com.kosta.databaseserver.spring.repo.abs.postgr.PostgrBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

public class PostgrBoardRepository implements AsyncBoardRepository {
    private static final AtomicLong SEQ = new AtomicLong(0);

    @Autowired
    private PostgrBoard postgrBoard;

    public Mono<ResponseEntity<Board>> create(Board data) {
        Board newBoard = new Board().copy(data);
        newBoard.setId( SEQ.incrementAndGet() );

        return postgrBoard.save(newBoard).map(ResponseEntity::ok);
    }


    @Override
    public Mono<ResponseEntity<Board>> read(Long id) {
        return postgrBoard.findById(id).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Board>> update(Long id, Board data) {
        return postgrBoard.findById(id).map( it -> ResponseEntity.ok( it.copy(data) ));
    }

    @Override
    public Mono<ResponseEntity<Boolean>> delete(Long id) {
        return postgrBoard.deleteById(id).map( it-> ResponseEntity.ok( true ) );
    }
}
