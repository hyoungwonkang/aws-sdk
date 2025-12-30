package com.example.dynamodb.dto;

public record PageResponse<T>(
     String userId,
     Long viewTime
) {}