package com.example.notify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.pinpointsmsvoicev2.PinpointSmsVoiceV2Client;
import software.amazon.awssdk.services.s3.S3Client;
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
    DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        DynamoDbClient ddb = DynamoDbClient.builder()
            .region(SEOUL_REGION)
            .build();
        DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build();
        return enhancedClient;
    }

    @Bean
    S3Client s3Client() {
        return S3Client.builder()
                .region(SEOUL_REGION)
                .build();
    }

    @Bean
    SesClient sesClient() {
        return SesClient.builder()
                .region(SEOUL_REGION)
                .build();
    }

    @Bean
    PinpointSmsVoiceV2Client smsClient() {
        return PinpointSmsVoiceV2Client.builder()
                .region(SEOUL_REGION)
                .build();
    }
}
