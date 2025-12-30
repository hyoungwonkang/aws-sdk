package com.example.s3.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private final String bucket = "my-bucket-khw-251230";

    public String getPreSignedUploadUrl(String originalFilename, String contentType) {
        String key = UUID.randomUUID() + "_" + originalFilename;
        PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(contentType)
            .build();
        
        PutObjectPresignRequest objectPresignRequest = PutObjectPresignRequest.builder()
            .putObjectRequest(objectRequest)
            .signatureDuration(Duration.ofMinutes(5))
            .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(objectPresignRequest);

        return presignedRequest.url().toString();
    }

    public ResponseInputStream<GetObjectResponse> getFileInputStream(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();
        return s3Client.getObject(getObjectRequest);
    }
}
