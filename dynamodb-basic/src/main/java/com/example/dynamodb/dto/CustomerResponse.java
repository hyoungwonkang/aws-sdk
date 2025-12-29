package com.example.dynamodb.dto;

public record CustomerResponse(
    String customerId,
    String name,
    String email,
    Long createdAt
) {}
