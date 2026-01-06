package com.example.sqs.dto;

import java.util.List;

public record CreateOrderRequest(
    String email,
    List<Item> items
) {}