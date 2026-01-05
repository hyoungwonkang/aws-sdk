package com.example.sqs.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.sqs.dto.CreateOrderRequest;
import com.example.sqs.dto.Item;
import com.example.sqs.dto.MessageDto;
import com.example.sqs.dto.OrderResponse;
import com.example.sqs.entity.ShopData;
import com.example.sqs.repository.ShopDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final SqsClient sqsClient;
  private final SecretService secretService;
  private final ObjectMapper objectMapper;    // 잭슨 라이브러리 사용 (gson을 써도 무관)

  private final ShopDataRepository repository;

  private static final String ITEM_PREFIX = "ITEM#";

  public String createOrder(List<Item> items) {
    // 주문 ID 생성
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    String uuid = UUID.randomUUID().toString().substring(0, 4);  // 첫 4글자만 사용
    String orderId = "ORD_" + timestamp + "_" + uuid;

    // 주문 총액
    Long totalAmount = items.stream()
      .mapToLong(item -> item.price())  // .mapToLong(Item::price)
      .sum();

    // 주문 정보 + 주문 상품 합쳐서 Repository로 전달
    List<ShopData> shopDataList = new ArrayList<>();

    // 주문 정보
    ShopData orderInfo = ShopData.builder()
      .pk(orderId)
      .sk("INFO")
      .type("ORDER")
      .info("ORDER_CREATED")
      .amount(totalAmount)
      .build();
    shopDataList.add(orderInfo);

    // 주문 상품
    for (int i = 0, size = items.size(); i < size; i++) {
      Item item = items.get(i);
      ShopData itemEntity = ShopData.builder()
        .pk(orderId)
        .sk(ITEM_PREFIX + String.format("%03d", i + 1))
        .type("ITEM")
        .info(item.name())
        .amount(item.price())
        .build();
      shopDataList.add(itemEntity);
    }

    // 트랜잭션을 위해서 한 번에 전달
    repository.save(shopDataList);

    // 주문 ID 반환
    return orderId;
  }

  public OrderResponse createOrder(CreateOrderRequest request) {
    // 주문 ID 생성
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    String uuid = UUID.randomUUID().toString().substring(0, 4);  // 첫 4글자만 사용
    String orderId = "ORD_" + timestamp + "_" + uuid;

    // 주문 총액
    Long totalAmount = request.items().stream()
      .mapToLong(item -> item.price())
      .sum();

    // 주문 정보 + 주문 상품 합쳐서 Repository로 전달
    List<ShopData> shopDataList = new ArrayList<>();

    // 주문 정보
    ShopData orderInfo = ShopData.builder()
      .pk(orderId)
      .sk("INFO")
      .type("ORDER")
      .info("ORDER_CREATED")
      .amount(totalAmount)
      .build();
    shopDataList.add(orderInfo);

    // 주문 상품
    for (int i = 0, size = request.items().size(); i < size; i++) {
      Item item = request.items().get(i);
      ShopData itemEntity = ShopData.builder()
        .pk(orderId)
        .sk(ITEM_PREFIX + String.format("%03d", i + 1))
        .type("ITEM")
        .info(item.name())
        .amount(item.price())
        .build();
      shopDataList.add(itemEntity);
    }

    // 트랜잭션을 위해서 한 번에 전달
    repository.save(shopDataList);

    // SQS: Producer

    // 큐(SQS)에 저장할 메시지 만들기
    // 주문ID, 주문총액, 휴대전화번호, 이메일 정보 -> JSON 문자열 (JSON 라이브러리 사용)
    String payload;
    try {
      // MessageDto
      MessageDto dto = new MessageDto(
        orderId,
        totalAmount,
        request.phone(),
        request.email()
      );
      //MessageDto to JSON String
      payload = objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      payload = String.format(
        "{\"orderId\": \"%s\", \"totalAmount\": %d, \"phone\": \"%s\", \"email\": \"%s\"}",
        orderId,
        totalAmount,
        request.phone(),
        request.email()
      );
    }

    String QUEUE_URL = secretService.getSecret("QUEUE_URL");

    // 큐(SQS)에 메시지 저장 (비동기로 동작)
    SendMessageRequest messageRequest = SendMessageRequest.builder()
      .queueUrl(QUEUE_URL)
      .messageBody(payload) // JSON 문자열
      .build();
    sqsClient.sendMessage(messageRequest);

    // 결과 DTO 반환
    return new OrderResponse(orderId, totalAmount, "Order Created!");
  }
}