package com.example.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductHistory {
    private String userId;
    private String viewTime;
    private String productId;
    private String productName;
    private Long price;

    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }
    @DynamoDbSortKey
    public String getViewTime() {
        return viewTime;
    }
}