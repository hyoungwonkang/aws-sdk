package com.example.dynamodb.dto;

public record ProductViewRequest(
    String userId,
    String productId,
    String productName,
    Integer price
) {
}