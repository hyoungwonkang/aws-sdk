package com.example.contest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.contest.dto.ContestRequest;
import com.example.contest.dto.ContestResponse;
import com.example.contest.service.ContestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ContestController {

    private final ContestService contestService;

    @PostMapping("/api/contests")
    public ResponseEntity<ContestResponse> upload(
        @RequestPart("photo") MultipartFile photo,
        @RequestPart("data") ContestRequest data) {
            try {
                ContestResponse response = contestService.uploadFile(photo, data);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).build();
            }
    }
}
