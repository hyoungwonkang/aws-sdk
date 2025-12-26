package com.example.s3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Bean
    S3Presigner s3Presigner() {
        return S3Presigner.builder()
            .region(Region.AP_NORTHEAST_2)
            
            .build();
    }
}
