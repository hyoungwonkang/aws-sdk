package com.example.dynamodb.dto;

public record OrderResponse(
    String orderId,
    Long totalAmount,
    String message
) {}
