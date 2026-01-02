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

  @PostMapping
  public ResponseEntity<String> save(@RequestBody CustomerCreateRequest request) {
    String customerId = customerService.saveCustomer(
      request.name(),
      request.email()
    );

    return ResponseEntity.status(201).body(customerId);
  }

  @GetMapping("/{customerId}")
  public ResponseEntity<CustomerResponse> find(@PathVariable("customerId") String customerId) {
    Customer foundCustomer = customerService.getCustomer(customerId);

    CustomerResponse response = new CustomerResponse(
      foundCustomer.getCustomerId(),
      foundCustomer.getName(),
      foundCustomer.getEmail(),
      foundCustomer.getCreatedAt()
    );

    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{customerId}")
  public ResponseEntity<Void> delete(@PathVariable String customerId) {
    customerService.deleteCustomer(customerId);
    return ResponseEntity.status(204).build();
  }

  @PutMapping("/{customerId}")
  public ResponseEntity<CustomerResponse> updateCustomer(
    @PathVariable String customerId,
    @RequestBody CustomerCreateRequest request
  ) {
    Customer updated = customerService.updateCustomer(customerId, request);
    if (updated == null) {
      return ResponseEntity.status(204).build();
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