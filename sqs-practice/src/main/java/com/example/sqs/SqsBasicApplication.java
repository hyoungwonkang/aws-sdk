package com.example.sqs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling	// @Scheduled 허용 어노테이션
public class SqsBasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqsBasicApplication.class, args);
	}

}
