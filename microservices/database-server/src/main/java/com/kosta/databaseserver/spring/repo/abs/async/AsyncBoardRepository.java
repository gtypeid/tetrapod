package com.kosta.databaseserver.spring.repo.abs.async;

import com.kosta.common.spring.data.vo.Board;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface AsyncBoardRepository {

    public Mono<ResponseEntity<Board>> create(Board data);
    public Mono<ResponseEntity<Board>> read(Long id);
    public Mono<ResponseEntity<Board>> update(Long id, Board data);
    public Mono<ResponseEntity<Boolean>> delete(Long id);
}
