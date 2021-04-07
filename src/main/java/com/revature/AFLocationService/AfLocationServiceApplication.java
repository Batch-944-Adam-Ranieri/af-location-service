package com.revature.AFLocationService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.revature.repos"})
@EntityScan(basePackages = {"com.revature.entities"})
@ComponentScan(basePackages = "com.revature")
public class AfLocationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AfLocationServiceApplication.class, args);
	}

}
