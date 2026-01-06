package com.example.contest.dto;

public record ContestResponse(
    String entryId,
    String name,
    String title,
    String photoUrl,
    String status
) {}