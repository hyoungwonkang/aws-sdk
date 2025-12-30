package com.example.dynamodb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dynamodb.dto.OrderRequest;
import com.example.dynamodb.dto.OrderResponse;
import com.example.dynamodb.service.ProductHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class ProductHistoryController {

    private final ProductHistoryService historyService;

    @PostMapping
    public String save(@RequestBody OrderRequest request) {
        historyService.saveHistory(request.userId(), request.productId(), request.productName(), request.price());
        return "Saved!";
    }

    @GetMapping("/{userId}")
    public List<OrderResponse> getHistories(@PathVariable String userId) {
        return historyService.getUserHistories(userId);
    }
}