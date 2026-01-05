package com.example.notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DynamodbSingletablePracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamodbSingletablePracticeApplication.class, args);
	}

}
