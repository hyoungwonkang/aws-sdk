package com.example.contest.service;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.contest.dto.ContestRequest;
import com.example.contest.dto.ContestResponse;
import com.example.contest.entity.Entry;
import com.example.contest.repository.ContestRepository;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
@RequiredArgsConstructor
public class ContestService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucket = "contest-bucket-khw2"; // 실제 버킷 이름으로 변경 필요

    private final ContestRepository contestRepository;

    public ContestResponse uploadFile(MultipartFile photo, ContestRequest request) throws IOException {
        String entryId = UUID.randomUUID().toString();  // 접수번호 생성
        
        String key = "entries/" + entryId + "_" + photo.getOriginalFilename();

        PutObjectRequest objectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType(photo.getContentType())
            .build();

        s3Client.putObject(
            objectRequest, 
            RequestBody.fromInputStream(photo.getInputStream(), photo.getSize())
        );

        // GET용 Presigned URL 생성 (조회/다운로드용)
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build();

        GetObjectPresignRequest getPresignRequest = GetObjectPresignRequest.builder()
            .getObjectRequest(getObjectRequest)
            .signatureDuration(Duration.ofDays(7))  // 7일간 유효
            .build();
        
        PresignedGetObjectRequest presignedGetRequest = s3Presigner.presignGetObject(getPresignRequest);

        // DB에 저장
        Entry entry = Entry.builder()
            .entryId(entryId)
            .name(request.name())
            .phoneNumber(request.phoneNumber())
            .email(request.email())
            .title(request.title())
            .build();

        contestRepository.save(entry);

        // 응답 생성 - GET용 Presigned URL 반환
        String photoUrl = presignedGetRequest.url().toString();
        
        return new ContestResponse(
            entryId,
            request.name(),
            request.title(),
            photoUrl,
            "SUBMITTED"
        );
    }
}
