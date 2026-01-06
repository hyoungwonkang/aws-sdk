package com.example.dynamodb.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.dynamodb.dto.EmailRequest;

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
    private final String SENDER = "test@gmail.com";
    
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
                .source(SENDER)
                .destination(destination)
                .message(message)
                .build();
        // 6. 이메일 전송!
        sesClient.sendEmail(emailRequest);
        // 7. 반환
        return "받는 사람: " + request.to();
    }
    
    @Async
    public void sendEmailAsync(String to, String subject, String body) {
        EmailRequest request = new EmailRequest(to, subject, body);
        sendEmail(request);
    }
}
