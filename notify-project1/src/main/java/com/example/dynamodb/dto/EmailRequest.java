package com.example.dynamodb.dto;

public record EmailRequest(
    String to, // 받는 사람
    String subject, // 제목
    String body // 내용
) {}
