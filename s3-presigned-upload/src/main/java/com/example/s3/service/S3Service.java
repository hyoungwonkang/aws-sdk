package com.example.s3.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Presigner s3Presigner;
    private final String bucket = "my-bucket-khw-251226";

    public String getPresignedUrl(String key, String contentType) {
        // 일단, PutObjectRequest 생성
        PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(contentType)
            .build();
        // 프리사인 URL 요청을 위한 PutObjectPresignRequest 생성
        PutObjectPresignRequest objectPresignRequest = PutObjectPresignRequest.builder()
            .putObjectRequest(objectRequest)
            .signatureDuration(Duration.ofMinutes(5))   // 5분간만 유효한 URL 반환
            .build();
        // Presigned URL 요청
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(objectPresignRequest);
        // URL만 빼서 반환
        return presignedRequest.url().toString();
    }
}
