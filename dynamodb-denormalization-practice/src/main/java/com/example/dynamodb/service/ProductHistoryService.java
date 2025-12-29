package com.example.dynamodb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dynamodb.dto.ProductViewRequest;
import com.example.dynamodb.dto.ProductViewResponse;
import com.example.dynamodb.entity.ProductHistory;
import com.example.dynamodb.repository.ProductHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductHistoryService {

    private final ProductHistoryRepository productHistoryRepository;

    public void saveProductView(ProductViewRequest request) {
        if (request.userId() == null || request.userId().isEmpty()) {
            throw new IllegalArgumentException("userId cannot be null or empty");
        }
        ProductHistory productHistory = new ProductHistory(
            request.userId(),
            System.currentTimeMillis(),
            request.productId(),
            request.productName(),
            request.price()
        );
        productHistoryRepository.save(productHistory);
    }

    public List<ProductViewResponse> getProductViewsByUserId(String userId) {
        List<ProductHistory> histories = productHistoryRepository.findByUserId(userId);
        return histories.stream()
            .map(history -> new ProductViewResponse(
                history.getUserId(),
                history.getTimestamp(),
                history.getProductId(),
                history.getProductName(),
                history.getPrice()
            ))
            .collect(Collectors.toList());
    }
}