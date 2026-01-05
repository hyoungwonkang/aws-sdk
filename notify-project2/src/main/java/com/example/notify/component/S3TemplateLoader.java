package com.example.notify.component;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Component
@RequiredArgsConstructor
public class S3TemplateLoader {
    
    private final S3Client s3Client;

    private final String WELCOME_BUCKET = "my-template-bucket-khw";
    private final String WELCOME_KEY = "welcome.html";

    public String loadWelcomeTemplate() {
        try {
            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(
                b -> b.bucket(WELCOME_BUCKET)
                    .key(WELCOME_KEY)
            );
            return objectBytes.asUtf8String(); // welcome.html 파일을 UTF-8로 저장했어야 함.
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("S3 welcome.html 로딩 실패!");
            return "<html><body><h1>환영합니다 {name}님</h1></body></html>";
        }
    }
}
