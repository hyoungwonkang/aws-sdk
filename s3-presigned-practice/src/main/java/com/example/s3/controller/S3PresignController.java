package com.example.s3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.s3.service.S3Service;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
@RequiredArgsConstructor
public class S3PresignController {

    private final S3Service s3Service;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> download(@RequestParam("key") String key) {
        System.out.println("Download requested for key: " + key);
        try {
            var responseInputStream = s3Service.downloadObject(key);
            StreamingResponseBody stream = outputStream -> {
                responseInputStream.transferTo(outputStream);
                responseInputStream.close();
            };
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + key + "\"")
                .body(stream);
        } catch (Exception e) {
            System.out.println("Download failed for key: " + key + ", error: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/presigned-url")
    @ResponseBody
    public String presignedUrl(
        @RequestParam("key") String key,
        @RequestParam("contentType") String contentType
    ){
        return s3Service.getPresignedUrl(key, contentType);
    }
    
}
