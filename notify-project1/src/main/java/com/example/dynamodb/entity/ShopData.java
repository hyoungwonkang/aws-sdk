package com.example.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDbBean // @Entity 역할
public class ShopData {
    private String pk;
    private String sk;
    private String type;
    private String info;
    private Long amount;
    private String email;

    @DynamoDbPartitionKey // @Id 역할
    @DynamoDbAttribute("PK")  // 실제 DB 칼럼명은 PK
    public String getPk() {
        return pk;
    }
    
    @DynamoDbSortKey
    @DynamoDbAttribute("SK")  // 실제 DB 칼럼명은 SK
    public String getSk() {
        return sk;
    }
}
