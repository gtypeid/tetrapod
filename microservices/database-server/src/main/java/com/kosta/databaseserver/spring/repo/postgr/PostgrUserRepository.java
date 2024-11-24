package com.kosta.databaseserver.spring.repo.postgr;

import com.kosta.common.spring.data.vo.User;
import com.kosta.databaseserver.spring.repo.abs.async.AsyncUserRepository;
import com.kosta.databaseserver.spring.repo.abs.postgr.PostgrUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

public class PostgrUserRepository implements AsyncUserRepository {
    private static final AtomicLong SEQ = new AtomicLong(0);

    @Autowired
    private PostgrUser postgrUser;

    public Mono<ResponseEntity<User>> create(User data) {
        User newUser = new User().copy(data);
        //newUser.setTestSeq( SEQ.incrementAndGet() );

        return postgrUser.save(newUser).map(ResponseEntity::ok);
    }


    @Override
    public Mono<ResponseEntity<User>> read(Long id) {
        return postgrUser.findById(id).map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<User>> update(Long id, User data) {
        return postgrUser.findById(id).map( it -> ResponseEntity.ok( it.copy(data) ));
    }

    @Override
    public Mono<ResponseEntity<Boolean>> delete(Long id) {
        return postgrUser.deleteById(id).map( it-> ResponseEntity.ok( true ) );
    }
}
