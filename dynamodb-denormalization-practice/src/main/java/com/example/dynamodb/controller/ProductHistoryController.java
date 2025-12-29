package com.example.dynamodb.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dynamodb.dto.ProductViewRequest;
import com.example.dynamodb.dto.ProductViewResponse;
import com.example.dynamodb.service.ProductHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
public class ProductHistoryController {

    private final ProductHistoryService productHistoryService;

    @PostMapping
    public ResponseEntity<String> saveProductView(@RequestBody ProductViewRequest request) {
        productHistoryService.saveProductView(request);
        return ResponseEntity.status(201).body("Saved!");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ProductViewResponse>> getProductViews(@PathVariable String userId) {
        List<ProductViewResponse> responses = productHistoryService.getProductViewsByUserId(userId);
        return ResponseEntity.ok(responses);
    }
}