package com.kosta.databaseserver.spring.repo.abs.async;

import com.kosta.common.spring.data.vo.User;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface AsyncUserRepository {

    public Mono<ResponseEntity<User>> create(User data);
    public Mono<ResponseEntity<User>> read(Long id);
    public Mono<ResponseEntity<User>> update(Long id, User data);
    public Mono<ResponseEntity<Boolean>> delete(Long id);
}
