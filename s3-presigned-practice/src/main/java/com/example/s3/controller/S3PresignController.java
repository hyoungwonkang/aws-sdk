package com.example.s3.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.example.s3.service.S3Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RestController
@RequiredArgsConstructor
public class S3PresignController {

    private final S3Service s3Service;

    @GetMapping("/presigned-url")
    public ResponseEntity<String> getPresignedUrl(
        @RequestParam String filename, 
        @RequestParam String contentType
    ) {
        return ResponseEntity.ok(s3Service.getPreSignedUploadUrl(filename, contentType));
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> download(@RequestParam("key") String key) {
        ResponseInputStream<GetObjectResponse> s3InputStream = s3Service.getFileInputStream(key);
        
        
        StreamingResponseBody responseBody = outputStream -> {
            try (s3InputStream) {
                s3InputStream.transferTo(outputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        GetObjectResponse getObjectResponse = s3InputStream.response();
        key = key.substring(key.indexOf("_") + 1);
        String filename = URLEncoder
        .encode(key, StandardCharsets.UTF_8)  // 파일 이름을 UTF-8 인코딩
        .replaceAll("\\+", "%20");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition
            .attachment()
            .filename(filename)
            .build());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(getObjectResponse.contentLength());

        return ResponseEntity.ok()
            .headers(headers)
            .body(responseBody);
    }
}