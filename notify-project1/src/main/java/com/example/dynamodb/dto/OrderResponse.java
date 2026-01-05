package com.example.dynamodb.dto;

public record CreateOrderResponse(
    String orderId,
    Long totalAmount,
    String message
) {}
