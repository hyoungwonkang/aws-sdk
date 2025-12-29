package com.example.s3.controller;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.s3.model.S3FileDto;
import com.example.s3.service.S3Service;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Controller
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<S3FileDto> files = s3Service.listFiles();
        model.addAttribute("files", files);
        return "index";
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> download(@RequestParam("key") String key) {
        try {
            ResponseBytes<GetObjectResponse> bytes = s3Service.download(key);

            String originalFilename = key.substring(key.indexOf("_") + 1);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", originalFilename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource(bytes.asByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
