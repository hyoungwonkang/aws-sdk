package com.example.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean // @Entity 역할
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Customer {
    private String customerId;
    private String name;
    private String email;
    private Long createdAt; // Timestamp

    @DynamoDbPartitionKey // @Id 역할
    public String getCustomerId() {
        return customerId;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public Long getCreatedAt() {
        return createdAt;
    }
}
