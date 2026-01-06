package com.example.contest.repository;

import org.springframework.stereotype.Repository;

import com.example.contest.entity.Entry;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
public class ContestRepository {
    
    private final DynamoDbEnhancedClient enhancedClient;    
    private final String TABLE_NAME = "ContestTable-khw2";
    private DynamoDbTable<Entry> entryTable;  

    @PostConstruct
    public void init() {
        entryTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(Entry.class));
    }

    public void save(Entry entry) {
        entryTable.putItem(entry);
    }
}
