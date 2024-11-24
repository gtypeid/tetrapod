package com.kosta.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import reactor.blockhound.BlockHound;

@SpringBootApplication(scanBasePackages = {"com.kosta.common", "com.kosta.apiserver"})
public class ApiServerApplication {

	public static void main(String[] args) {
		//BlockHound.install();
		SpringApplication.run(ApiServerApplication.class, args);
	}

}
