package com.example.contest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AwsSdkConfig {
    private final Region SEOUL_REGION = Region.AP_NORTHEAST_2;
    
    @Bean
    S3Client s3Client() {
        return S3Client.builder()
                .region(SEOUL_REGION)
                .build();
    }

    @Bean
    S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(SEOUL_REGION)   
                .build();
    }

    @Bean
    DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(SEOUL_REGION)
                .build();
    }

    @Bean
    DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient())
                .build();
    }
    
    @Bean
    SesClient sesClient() {
        return SesClient.builder()
                .region(SEOUL_REGION)
                .build();
    }

    @Bean
    SqsClient sqsClient() {
        return SqsClient.builder()
                .region(SEOUL_REGION)
                .build();
    }

    @Bean
    SecretsManagerClient secretsManagerClient() {
        return SecretsManagerClient.builder()
                .region(SEOUL_REGION)
                .build();
    }
}
