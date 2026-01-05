package com.example.notify.repository;

import org.springframework.stereotype.Repository;

import com.example.notify.entity.Member;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final String TABLE_NAME = "Member-khw";
    private DynamoDbTable<Member> memberTable;  

    @PostConstruct
    public void init() {
        memberTable = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(Member.class));
    }

    public void save(Member member) {
        memberTable.putItem(member);
    }
}