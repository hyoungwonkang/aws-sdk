package com.example.sqs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sqs.dto.CreateOrderRequest;
import com.example.sqs.dto.OrderResponse;
import com.example.sqs.dto.EmailRequest;
import com.example.sqs.service.NotificationService;
import com.example.sqs.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;
  private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
      String newOrderId = orderService.createOrder(request.items());
      OrderResponse response = new OrderResponse(newOrderId, 1L ,"Order Created!");
      
      // 비동기로 이메일 전송
      notificationService.sendEmailAsync("hwplus@gmail.com", "주문 완료", "주문번호: " + newOrderId);
      
      return ResponseEntity.status(201).body(response);
    }
    
    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest request) {
      return ResponseEntity.ok(notificationService.sendEmail(request));
    }
}