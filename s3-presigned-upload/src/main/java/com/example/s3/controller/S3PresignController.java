package com.example.s3.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.s3.service.S3Service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class S3PresignController {

    private final S3Service s3Service;

    @GetMapping("/presigned-url")
    public String presignedUrl(
        @RequestParam("key") String key,
        @RequestParam("contentType") String contentType
    ){
        return s3Service.getPresignedUrl(key, contentType);
    }
    
}
