package com.kosta.databaseserver.spring.repo.abs.async;

import com.kosta.common.spring.data.vo.Reply;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface AsyncReplyRepository {

    public Mono<ResponseEntity<Reply>> create(Reply data);
    public Mono<ResponseEntity<Reply>> read(Long id);
    public Mono<ResponseEntity<Reply>> update(Long id, Reply data);
    public Mono<ResponseEntity<Boolean>> delete(Long id);
}
