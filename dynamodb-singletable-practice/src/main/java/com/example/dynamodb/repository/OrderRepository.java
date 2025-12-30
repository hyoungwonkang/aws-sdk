package com.example.dynamodb.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.dynamodb.entity.Order;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<Order> orderTable;

    @PostConstruct
    public void init() {
        this.orderTable = enhancedClient.table("Shopdata-khw", TableSchema.fromBean(Order.class));
    }

    public void save(Order order) {
        orderTable.putItem(order);
    }

    public List<Order> findByUserId(String userId, boolean scanIndexForward) {
        Key key = Key.builder()
            .partitionValue(userId)
            .build();

        QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
       
        return orderTable.query(req -> req
            .queryConditional(queryConditional)
            .scanIndexForward(scanIndexForward))
            .items()
            .stream()
            .collect(Collectors.toList());
    }
}