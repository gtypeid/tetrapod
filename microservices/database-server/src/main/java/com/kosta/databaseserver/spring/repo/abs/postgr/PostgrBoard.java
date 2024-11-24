package com.kosta.databaseserver.spring.repo.abs.postgr;

import com.kosta.common.spring.data.vo.Board;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PostgrBoard extends ReactiveCrudRepository<Board, Long> {
}
