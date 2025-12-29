package com.example.dynamodb.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.dynamodb.entity.ProductHistory;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

@Repository
@RequiredArgsConstructor
public class ProductHistoryRepository {

    private final DynamoDbEnhancedClient enhancedClient;

    private DynamoDbTable<ProductHistory> productHistoryTable;

    @PostConstruct
    public void init() {
        productHistoryTable = enhancedClient.table("ProductHistory-khw", TableSchema.fromBean(ProductHistory.class));
    }

    public void save(ProductHistory productHistory) {
        productHistoryTable.putItem(productHistory);
    }

    public List<ProductHistory> findByUserId(String userId) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder().partitionValue(userId).build());
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
            .queryConditional(queryConditional)
            .scanIndexForward(false) // 내림차순
            .build();

        return productHistoryTable.query(queryRequest)
            .items()
            .stream()
            .collect(Collectors.toList());
    }
}