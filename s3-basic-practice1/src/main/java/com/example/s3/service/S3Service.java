package com.example.s3.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

  private final S3Client s3Client;
  private final String bucket = "my-bucket-khw-251226";  // AWS Console - S3 - 버킷 직접 생성

  public S3Service(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  // [업로드]
  public String uploadFile(MultipartFile file) throws IOException {
    // 매일 새로운 폴더 생성
    // 버킷 내 파일명 중복 방지를 위해 파일명에 UUID 추가
    String key = DateTimeFormatter.ofPattern("yyyy/MM/dd/").format(LocalDate.now())
      + UUID.randomUUID()
      + "_"  // 실제 파일명을 알고 싶을 때 사용하는 구분자: Underscore
      + file.getOriginalFilename();

    // 메타데이터 설정 (이게 있어야 브라우저에서 바로 보입니다)
    PutObjectRequest objectRequest = PutObjectRequest.builder()
      .bucket(bucket)
      .key(key)
      .contentType(file.getContentType())
      .build();

    // 업로드 실행
    // RequestBody.fromInputStream: 파일을 디스크에 저장하지 않고 메모리로 바로 보냅니다.
    // 주의: 파일 크기(file.getSize())를 반드시 알려줘야 합니다.
    s3Client.putObject(
	    objectRequest, 
	    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
	  );

    // 접근 가능한 URL 반환
    return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + key;
  }
}
