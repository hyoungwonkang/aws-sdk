package com.example.dynamodb.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.dynamodb.dto.PageResponse;
import com.example.dynamodb.dto.ProductHistoryResponse;
import com.example.dynamodb.entity.ProductHistory;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

@Repository
@RequiredArgsConstructor
public class ProductHistoryRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<ProductHistory> historyTable;

    @PostConstruct
    public void init() {
        this.historyTable = enhancedClient.table("ProductHistory-khw", TableSchema.fromBean(ProductHistory.class));
    }

    public void save(ProductHistory history) {
        historyTable.putItem(history);
    }

    public List<ProductHistory> findByUserId(String userId, boolean scanIndexForward) {
        Key key = Key.builder()
            .partitionValue(userId)
            .build();

        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
       
        return historyTable.query(req -> req
            .queryConditional(queryConditional)
            .scanIndexForward(scanIndexForward))
            .items()
            .stream()
            .collect(Collectors.toList());
    }

    
    public PageResponse<ProductHistoryResponse> findPagedPagedHistoriesByUserId(String userId, int limit, String lastViewTime) {
        // Key
        Key key = Key.builder()
            .partitionValue(userId)
            .build();
    }
}