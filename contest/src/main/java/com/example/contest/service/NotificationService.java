package com.example.contest.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.ses.SesClient;

@Service
@RequiredArgsConstructor
public class NotificationService {
  
    private final SesClient sesClient;
    private final SecretService secretService;

    public String sendEmail() {
        
    }
}
