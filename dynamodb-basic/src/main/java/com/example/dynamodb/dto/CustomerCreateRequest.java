package com.example.dynamodb.dto;

public record CustomerCreateRequest(
    String name,
    String email
) {}
