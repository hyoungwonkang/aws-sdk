package com.example.dynamodb.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.dynamodb.dto.CustomerCreateRequest;
import com.example.dynamodb.entity.Customer;
import com.example.dynamodb.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;

  // [저장하기]
  // PK 생성 후 반환
  public String saveCustomer(String name, String email) {
    // PK 생성 (UUID)
    String customerId = UUID.randomUUID().toString();
    // 엔티티 생성
    Customer customer = new Customer(
      customerId, 
      name, 
      email, 
      System.currentTimeMillis()  // DB단에서 넣을 수 있음
    );
    // 저장 및 PK 반환
    return customerRepository.save(customer);
  }

  // 조회하기
  public Customer getCustomer(String customerId) {
    Customer foundCustomer = customerRepository.findById(customerId);
    if (foundCustomer == null) {
      throw new RuntimeException("Customer not found. customer ID: " + customerId);
    }
    return foundCustomer;
  }

  // 삭제하기
  public void deleteCustomer(String customerId) {
    // 조회 후 삭제해도 됨
    // Customer foundCustomer = customerRepository.findById(customerId);
    // if (foundCustomer == null) {
    //   throw new RuntimeException("Customer not found. customer ID: " + customerId);
    // }
    customerRepository.deleteById(customerId);
  }

  // 수정하기
  public Customer updateCustomer(String customerId, CustomerCreateRequest request) {
    // 수정할 엔티티 조회
    Customer existing = customerRepository.findById(customerId);
    // 수정할 필드 반영 (부분 업데이트)
    String name = request.name();
    if (name != null) {
      existing.setName(name);  // JPA처럼 변경 감지는 안 됩니다. (Dirty Checking 불가)
    }
    String email = request.email();
    if (email != null) {
      existing.setEmail(email);
    }
    return customerRepository.update(existing);
  }
}
