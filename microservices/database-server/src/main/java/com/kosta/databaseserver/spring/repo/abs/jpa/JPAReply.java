package com.kosta.databaseserver.spring.repo.abs.jpa;

import com.kosta.common.spring.data.vo.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAReply extends JpaRepository<Reply, Long> {
}
