package com.example.sqs.dto;

public record OrderResponse(
    String orderId,
    Long totalAmount,
    String message
) {}
