package com.kosta.loadbalancer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.kosta.common", "com.kosta.loadbalancer"})
public class LoadBalancerApplication {


	public static void main(String[] args) {
		SpringApplication.run(LoadBalancerApplication.class, args);
	}

}
