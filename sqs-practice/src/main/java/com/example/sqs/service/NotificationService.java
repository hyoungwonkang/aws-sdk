package com.example.sqs.service;

import org.springframework.stereotype.Service;

import com.example.sqs.dto.EmailRequest;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final SesClient sesClient;
    private final SecretService secretService;
    
    public String sendEmail(EmailRequest request) {
        // 1. 받는 사람
        Destination destination = Destination.builder()
                .toAddresses(request.to())
                .build();
        // 2. 제목
        Content subject = Content.builder()
                .data(request.subject())
                .build();
        // 3. 내용
        Content body = Content.builder()
                .data(request.body())
                .build();
        // 4. 제목 + 내용
        Message message = Message.builder()
                .subject(subject)
                .body(b -> b.text(body))
                .build();
        // 5. 이메일 전송 객체
        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .source(secretService.getSecret("SENDER_EMAIL"))
                .destination(destination)
                .message(message)
                .build();
        // 6. 이메일 전송!
        sesClient.sendEmail(emailRequest);
        // 7. 반환
        return "받는 사람: " + request.to();
    }
}
