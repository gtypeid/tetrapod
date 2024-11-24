package com.kosta.databaseserver.spring.repo.abs.jpa;

import com.kosta.common.spring.data.vo.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPABoard extends JpaRepository<Board, Long> {
}
