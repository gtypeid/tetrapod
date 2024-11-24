package com.kosta.databaseserver.spring.repo.abs.jpa;

import com.kosta.common.spring.data.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAUser extends JpaRepository<User, Long> {
}
