package com.example.sqs.dto;

public record MessageDto(
    String orderId,
    Long totalAmount,
    String email
) {}
