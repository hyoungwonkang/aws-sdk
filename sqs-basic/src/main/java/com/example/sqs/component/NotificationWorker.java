package com.example.sqs.component;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.sqs.dto.MessageDto;
import com.example.sqs.service.NotificationService;
import com.example.sqs.service.SecretService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

@Component
@RequiredArgsConstructor
public class NotificationWorker {

    private final SqsClient sqsClient;
    private final SecretService secretService;
    private final ObjectMapper objectMapper;    // 잭슨 라이브러리 사용 (gson을 써도 무관)
    private final NotificationService notificationService;


    @Scheduled(fixedDelay = 1000) // 큐의 메시지를 읽은 뒤, 1초 쉬기
    public void pollQueue() {
        // 큐 메시지 수신 객체 생성
        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(secretService.getSecret("QUEUE_URL"))
                .maxNumberOfMessages(10) // 한 번에 최대 10개 메시지 수신
                .waitTimeSeconds(20)    // 롱 폴링: 최대 20초 대기
                .build();
        
        // 큐 메시지 수신!
        List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();
        
        // 메시지 순회하며 처리
        for (Message message : messages) {
            try {
                // message == {"MessageId":"...","ReceiptHandle":"...","MD5OfBody":"...","Body":"{...}"}
                MessageDto messageDto = objectMapper.readValue(message.body(), MessageDto.class);

                // 내용 추출
                String orderId = messageDto.orderId();
                Long totalAmount = messageDto.totalAmount();
                String phone = messageDto.phone();
                String email = messageDto.email();

                // 이메일 보내기
                notificationService.sendEmailAsync(
                    email,
                    "[서비스명] 주문이 완료되었습니다", 
                    "주문ID: " + orderId + "\n총 금액: " + new DecimalFormat("#,##0").format(totalAmount) + "원"
                );

                notificationService.sendSmsAsync(
                    phone,
                    "주문ID: " + orderId + "\n총 금액: " + new DecimalFormat("#,##0").format(totalAmount) + "원"
                );


                // 큐 메시지 삭제 객체 생성
                DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                    .queueUrl(secretService.getSecret("QUEUE_URL"))
                    .receiptHandle(message.receiptHandle())
                    .build();

                // 큐 메시지 삭제!
                sqsClient.deleteMessage(deleteRequest);

                System.out.println("SQS 메시지 처리 완료: " + orderId);
            } catch (Exception e) {
                System.err.println("SQS 메시지 처리 실패: " + e.getMessage());
            }
        }
    }
}
