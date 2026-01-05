package com.example.notify.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class Member {
    private String memberId; // PK (UUID)
    private String name;
    private String email;
    private String phone;
    private String createdAt;

    @DynamoDbPartitionKey
    public String getMemberId() {
        return memberId;
    }
}
