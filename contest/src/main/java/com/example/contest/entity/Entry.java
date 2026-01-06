package com.example.contest.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class Entry {
    private String entryId;
    private String name;
    private String phoneNumber;
    private String email;
    private String title;

    @DynamoDbPartitionKey
    public String getEntryId() {
        return entryId;
    }
}
