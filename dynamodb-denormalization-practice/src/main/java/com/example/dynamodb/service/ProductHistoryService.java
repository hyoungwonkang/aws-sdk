package com.example.dynamodb.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dynamodb.dto.PageResponse;
import com.example.dynamodb.dto.ProductHistoryResponse;
import com.example.dynamodb.entity.ProductHistory;
import com.example.dynamodb.repository.ProductHistoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductHistoryService {

    private final ProductHistoryRepository repository;

    public void saveHistory(String userId, String productId, String productName, Long price) {

        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        String sortKey = timeStamp + "#" + productId;
        ProductHistory productHistory = new ProductHistory(userId, sortKey, productId, productName, price);
        
        repository.save(productHistory);
    }

    public List<ProductHistoryResponse> getUserHistories(String userId) {
        return repository.findByUserId(userId, false)
         .stream()
         .map(ProductHistoryResponse::new)
         .collect(Collectors.toList());
    }

    /**
     * 상품 조회 이력 반환 (페이징)
    */
    public PageResponse<> getPagedHistories(String userId, int limit, String lastViewTime) {
        return repository.findPagedPagedHistoriesByUserId(userId, limit, lastViewTime);
    }
}