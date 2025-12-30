package com.example.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean // @Entity 역할
public class Order {
    private String PK;
    private String SK;
    private String type;
    private String info;
    private Integer amount;

    @DynamoDbPartitionKey // @Id 역할
    public String getPK() {
        return PK;
    }
    @DynamoDbSortKey
    public String getSK() {
        return SK;
    }
    public String getType() {
        return type;
    }
    public String getInfo() {
        return info;
    }
    public Integer getAmount() {
        return amount;
    }
    public void setSKForInfo() {
        this.SK = "INFO";
    }
    public void setSKForItem(String itemId) {
        this.SK = "ITEM#" + itemId;
    }
}
