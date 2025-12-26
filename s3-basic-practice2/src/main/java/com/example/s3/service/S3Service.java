package com.example.s3.service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.s3.model.S3FileDto;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

@Service
public class S3Service {

  private final S3Client s3Client;
  private final String bucket = "my-bucket-khw-251226";  // AWS Console - S3 - 버킷 직접 생성

  public S3Service(S3Client s3Client) {
    this.s3Client = s3Client;
  }

    /**
     * S3 버킷에서 파일 목록을 조회합니다.
     * @return 파일 목록 (S3FileDto)
     */
    public List<S3FileDto> listFiles() {
        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                .bucket(bucket)
                .build();
            
            ListObjectsV2Response response = s3Client.listObjectsV2(request);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            return response.contents().stream()
                .map(s3Object -> new S3FileDto(
                    s3Object.key(), // getter
                    s3Object.size(),
                    s3Object.lastModified().atZone(ZoneId.systemDefault()).format(formatter)
                ))
                .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("S3 파일 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    // 다운로드
    public ResponseBytes<GetObjectResponse> download(String key) {
        // S3 객체 가져올 때 사용하는 GetObjectRequest
        GetObjectRequest objectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .build();
        
        // 실제로 가져와서 반환
        // toBytes(): 가져온 객체를 메모리에 로드
        // 서버의 메모리를 사용하는 방식이므로 대용량 파일 주의해야 함!
        // 다음에는 toInputString()으로 스트림만 연결해 주는 방식 사용할 예정
        return s3Client.getObject(objectRequest, ResponseTransformer.toBytes());
  }

}
