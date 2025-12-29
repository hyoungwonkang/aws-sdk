package com.example.dynamodb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dynamodb.dto.CustomerCreateRequest;
import com.example.dynamodb.dto.CustomerResponse;
import com.example.dynamodb.entity.Customer;
import com.example.dynamodb.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

  private final CustomerService customerService;

  // [저장하기]
  // POST /api/customers
  // Body {"name": "kim", "email": "kim@example.com"}
  @PostMapping
  public ResponseEntity<String> save(@RequestBody CustomerCreateRequest request) {
    String customerId = customerService.saveCustomer(
      request.name(),
      request.email()
    );
    // 201, 등록된 customerId 반환
    return ResponseEntity.status(201).body(customerId);
  }

  // [단 건 조회]
  // GET /api/customers/{customerId}
  @GetMapping("/{customerId}")
  public ResponseEntity<CustomerResponse> find(@PathVariable("customerId") String customerId) {
    // 조회
    Customer foundCustomer = customerService.getCustomer(customerId);
    if (foundCustomer == null) {
      return ResponseEntity.notFound().build();
    }
    // 엔티티 -> DTO
    CustomerResponse response = new CustomerResponse(
      foundCustomer.getCustomerId(), 
      foundCustomer.getName(), 
      foundCustomer.getEmail(), 
      foundCustomer.getCreatedAt()
    );
    // 응답 (200, 조회된 고객 정보)
    return ResponseEntity.ok(response);
  }

  // [삭제 요청]
  // DELETE /api/customers/{customerId}
  @DeleteMapping("/{customerId}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
    customerService.deleteCustomer(customerId);
    return ResponseEntity.noContent().build();  // 204
  }

  // [수정 요청]
  // PUT /api/customers/{customerId}
  // Body {"name": "kim", "email": "kim@example.com"}
  @PutMapping("/{customerId}")
  public ResponseEntity<CustomerResponse> updateCustomer(
    @PathVariable String customerId,
    @RequestBody CustomerCreateRequest request
  ) {
    Customer updated = customerService.updateCustomer(customerId, request);
    if (updated == null) {
      return ResponseEntity.notFound().build();
    }
    CustomerResponse response = new CustomerResponse(
      updated.getCustomerId(),
      updated.getName(),
      updated.getEmail(),
      updated.getCreatedAt()
    );
    return ResponseEntity.ok(response);
  }
}
