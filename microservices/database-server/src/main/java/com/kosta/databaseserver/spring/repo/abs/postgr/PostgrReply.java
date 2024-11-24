package com.kosta.databaseserver.spring.repo.abs.postgr;

import com.kosta.common.spring.data.vo.Reply;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PostgrReply extends ReactiveCrudRepository<Reply, Long> {
}
