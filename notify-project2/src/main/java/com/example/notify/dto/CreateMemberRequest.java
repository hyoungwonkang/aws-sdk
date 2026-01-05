package com.example.notify.dto;

public record CreateMemberRequest(
    String name,
    String email,
    String phone
) {}