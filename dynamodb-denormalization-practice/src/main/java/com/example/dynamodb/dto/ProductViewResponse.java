package com.example.dynamodb.dto;

public record ProductViewResponse(
    String userId,
    Long timestamp,
    String productId,
    String productName,
    Integer price
) {
}