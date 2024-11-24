package com.kosta.databaseserver.spring.repo.abs.postgr;

import com.kosta.common.spring.data.vo.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PostgrUser extends ReactiveCrudRepository<User, Long> {
}
