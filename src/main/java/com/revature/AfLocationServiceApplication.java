package com.revature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.revature.repos"})
@EntityScan(basePackages = {"com.revature.entities"})
@ComponentScan(basePackages = "com.revature")
public class AfLocationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AfLocationServiceApplication.class, args);
	}

}
