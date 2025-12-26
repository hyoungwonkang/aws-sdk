package com.example.s3.controller;

import org.springframework.web.bind.annotation.RestController;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class S3UploadController {
    
    private final S3Client s3Client;

    public S3UploadController(S3Client s3Client) {
        this.s3Client = s3Client;
    } // 스프링 생성자 주입 권장

    // [업로드: 버킷 생성 + 객체 업로드]
    @GetMapping("/upload")
    public String upload() {

        // 반환값
        String url = "Upload S3 File";

        // 버킷 이름 (전 세계 유일)
        String bucket = "khw" + UUID.randomUUID().toString();

        // 객체 이름
        String key = "uploads/서시.txt";

        try {
            // 로컬 파일 업로드
            String filename = "/home/user1/서시.txt";

            // 봉투 (PutObjectRequest)
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(MediaType.TEXT_PLAIN_VALUE) // 키워드 "text/plain" 넣어줌
                .build();
            
            // 내용물 (RequestBody)
            // RequestBody.fromFile(Paths.get(filename));
            s3Client.putObject(objectRequest,
                 RequestBody.fromFile(Paths.get(filename)));    // S3 업로드가 이뤄지는 곳

            // 반환값 만들기
            url = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + key;

        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }

        // 반환
        return url;

    }
    
}
