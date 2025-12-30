package com.example.dynamodb.dto;

public record OrderRequest(
    String PK,
    String SK,
    String type,
    String info,
    Integer amount
) {
}