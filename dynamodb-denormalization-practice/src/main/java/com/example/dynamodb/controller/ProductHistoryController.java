package com.example.dynamodb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dynamodb.dto.ProductHistoryCreateRequest;
import com.example.dynamodb.dto.ProductHistoryResponse;
import com.example.dynamodb.dto.PageResponse;
import com.example.dynamodb.service.ProductHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class ProductHistoryController {

    private final ProductHistoryService historyService;

    @PostMapping
    public String save(@RequestBody ProductHistoryCreateRequest request) {
        historyService.saveHistory(request.userId(), request.productId(), request.productName(), request.price());
        return "Saved!";
    }

    @GetMapping("/{userId}")
    public List<ProductHistoryResponse> getHistories(@PathVariable String userId) {
        return historyService.getUserHistories(userId);
    }

    // [이력 조회 요청 with Paging]
    // GET /history/u100?limit=2&lastViewTime=2023-10-01T10:00:00
    @GetMapping("/paged/{userId}")
    public PageResponse<ProductHistoryResponse> getPagedHistories(
        @PathVariable String userId,
        @RequestParam(defaultValue = "2") int limit,
        @RequestParam(required = false) String lastViewTime
    ) {
        return historyService.getPagedHistories(userId, limit, lastViewTime);      

    }
}