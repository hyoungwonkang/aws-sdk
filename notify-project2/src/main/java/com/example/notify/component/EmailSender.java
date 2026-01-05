package com.example.notify.component;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

@RequiredArgsConstructor
@Component
public class EmailSender {
    private final SesClient sesClient;
    private final String SENDER_EMAIL = "hwplus@gmail.com";

    public void sendEmail(String to, String body, boolean isHtmlContent) {
        try {
            Destination destination = Destination.builder().toAddresses(to).build();
            Content subject = Content.builder().data("[서비스명] 멤버십 가입을 환영합니다!").build();
            Content bodyContent = Content.builder().data(body).build();
        
            Message message = Message.builder()
                .subject(subject)
                .body(b -> {
                    if (isHtmlContent) {
                        b.html(bodyContent);
                    } else {
                        b.text(bodyContent);
                    }
                })
                .build();

            SendEmailRequest emailRequest = SendEmailRequest.builder()
                .source(SENDER_EMAIL)
                .destination(destination)
                .message(message)
                .build();

            sesClient.sendEmail(emailRequest);
            System.out.println("웰컴 이메일 전송 완료~ To: " + to);
        } catch (Exception e) {
            System.err.println("웰컴 이메일 발송 실패: " + e.getMessage());
        }
    }
}
