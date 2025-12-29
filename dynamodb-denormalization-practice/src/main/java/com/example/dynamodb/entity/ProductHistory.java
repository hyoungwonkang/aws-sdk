package com.example.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean // @Entity 역할
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ProductHistory {
    private String userId ;
    private Long timestamp ; // Timestamp
    private String productId;
    private String productName;
    private Integer price;

    @DynamoDbPartitionKey // @Id 역할
    public String getUserId() {
        return userId ;
    }
    @DynamoDbSortKey
    public Long getTimestamp() {
        return timestamp ;
    }
    public String getProductId() {
        return productId;
    }
    public String getProductName() {
        return productName;
    }
    public Integer getPrice() {
        return price;
    }
}
