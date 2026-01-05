package com.example.sqs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.regions.Region;

@Configuration
public class AwsSdkConfig {
    
    private final Region SEOUL_REGION = Region.AP_NORTHEAST_2;
    
    @Bean
    DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
            .region(SEOUL_REGION)
            .build();
    }
    
    // 향상 클라이언트
    @Bean
    DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();
    }

    @Bean
    SesClient sesClient() {
        return SesClient.builder()
                .region(SEOUL_REGION)
                .build();
    }

    @Bean
    SecretsManagerClient secretsClient() {
        return SecretsManagerClient.builder()
                .region(SEOUL_REGION)
                .build();
    }
}
