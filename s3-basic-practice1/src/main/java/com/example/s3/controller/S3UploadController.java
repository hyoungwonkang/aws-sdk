package com.example.s3.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.example.s3.service.S3Service;

@RestController
public class S3UploadController {

  private final S3Service s3Service;

  public S3UploadController(S3Service s3Service) {
    this.s3Service = s3Service;
  }

  // 업로드 폼 페이지
  @GetMapping("/")
  public RedirectView uploadForm() {
    return new RedirectView("/index.html");
  }

  // 파일 업로드 처리
  @PostMapping("/upload-file")
  public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
    try {
      String fileUrl = s3Service.uploadFile(file);
      return ResponseEntity.ok("업로드 성공! URL: " + fileUrl);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body("업로드 실패: " + e.getMessage());
    }
  }
}
