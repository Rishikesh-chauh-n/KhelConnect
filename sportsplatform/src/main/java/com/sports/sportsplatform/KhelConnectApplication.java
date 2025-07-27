package com.sports.sportsplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.sports.sportsplatform.Repository")
@EnableScheduling
public class KhelConnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(KhelConnectApplication.class, args);
	}

}
