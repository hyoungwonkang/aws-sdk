package com.example.dynamodb.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dynamodb.dto.OrderResponse;
import com.example.dynamodb.entity.Order;
import com.example.dynamodb.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductHistoryService {

    private final OrderRepository repository;

    public void saveHistory(String userId, String productId, String productName, Long price) {

        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        String sortKey = timeStamp + "#" + productId;
        Order productHistory = new Order(userId, sortKey, productId, productName, price);
        
        repository.save(productHistory);
    }

    public List<OrderResponse> getUserHistories(String userId) {
        return repository.findByUserId(userId, false)
         .stream()
         .map(OrderResponse::new)
         .collect(Collectors.toList());
    }
}