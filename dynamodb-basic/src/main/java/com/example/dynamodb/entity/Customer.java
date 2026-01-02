package com.example.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Customer {
    private String customerId;
    private String name;
    private String email;
    private Long createdAt;

    @DynamoDbPartitionKey
    public String getCustomerId() {
        return customerId;
    }
}