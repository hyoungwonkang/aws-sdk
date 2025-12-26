package com.example.s3.controller;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.s3.model.S3FileDto;
import com.example.s3.service.S3Service;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    // 서버사이드쪽 코드
    @GetMapping("/")
    public String index(Model model) {
        List<S3FileDto> files = s3Service.listFiles(); // S3에서 실제 파일 목록 조회
        model.addAttribute("files", files); // 화면으로 files 전달
        return "index"; // template/index.html 파일 이동
    }

     @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> download(@RequestParam("key") String key) {
        try {
        // S3 객체 가져오기
        ResponseBytes<GetObjectResponse> bytes = s3Service.download(key);

        // 원래 파일명 (추가로 인코딩 필요할 수 있음)
        String originalFilename = key.substring(key.indexOf("_") + 1);

        // 다운로드용 HTTP 헤더 설정 (org.springframework.http.HttpHeaders)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", originalFilename);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  // "application/octet-stream"

        // 응답 (다운로드)
        return ResponseEntity.ok()
            .headers(headers)
            .body(new ByteArrayResource(bytes.asByteArray()));
        } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.notFound().build();  // 서버 코드 오류가 없다면 S3 객체가 없을 것으로 판단
        }
    }
}
