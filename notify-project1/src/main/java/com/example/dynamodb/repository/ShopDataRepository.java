package com.example.dynamodb.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.dynamodb.entity.ShopData;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
public class ShopDataRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private DynamoDbTable<ShopData> shopTable;  

    @PostConstruct
    public void init() {
        shopTable = enhancedClient.table("History-khw", TableSchema.fromBean(ShopData.class));
    }

    public void save(List<ShopData> shopDataList) {
        enhancedClient.transactWriteItems(builder -> 
        shopDataList.forEach(shopData -> 
            builder.addPutItem(shopTable, shopData)  // 엔티티 하나씩 전달해서 저장
        )
        );
    }
}