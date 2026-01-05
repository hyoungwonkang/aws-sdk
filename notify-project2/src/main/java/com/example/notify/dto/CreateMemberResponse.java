package com.example.notify.dto;

public record CreateMemberResponse(
    String memberId,
    String message,
    String createdAt
) {}