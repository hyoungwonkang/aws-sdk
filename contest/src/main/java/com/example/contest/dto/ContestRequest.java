package com.example.contest.dto;

public record ContestRequest(
    String name,
    String phoneNumber,
    String email,
    String title
) {}