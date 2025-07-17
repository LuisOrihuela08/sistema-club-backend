package com.club.control;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SistemaClubBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaClubBackendApplication.class, args);
	}

}
