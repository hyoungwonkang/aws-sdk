package com.example.dynamodb.dto;

import com.example.dynamodb.entity.Order;

public record OrderResponse(
     String PK,
     String SK,
     String type,
     String info,
     Integer amount
) {
    public OrderResponse(Order entity) {
        this(entity.getPK(),
             entity.getSK(),
             entity.getType(),
             entity.getInfo(),
             entity.getAmount());
    }
}