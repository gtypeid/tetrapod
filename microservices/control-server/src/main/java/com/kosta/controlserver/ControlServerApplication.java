package com.kosta.controlserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.kosta.common", "com.kosta.controlserver"})
public class ControlServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControlServerApplication.class, args);
	}

}
