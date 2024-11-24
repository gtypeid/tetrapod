package com.kosta.databaseserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication(scanBasePackages = {"com.kosta.common", "com.kosta.databaseserver"})

@EntityScan(basePackages = "com.kosta.common.spring.data")
@EnableJpaRepositories(basePackages = "com.kosta.databaseserver.spring.repo.abs.jpa")
@EnableR2dbcRepositories(basePackages = "com.kosta.databaseserver.spring.repo.abs.postgr")
public class DatabaseServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseServerApplication.class, args);
	}

}
