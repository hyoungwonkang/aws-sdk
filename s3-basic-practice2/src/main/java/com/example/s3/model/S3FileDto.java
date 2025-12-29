package com.example.s3.model;

// record
// 모든 필드는 자동으로 private final 처리
// @NoArgsConstructor 미지원
// @AllArgsConstructor 지원
// @Setter 미지원
// @Getter 지원 (get 필요 없음)
// 빌더 쓰려면 쓸 수 있음
public record S3FileDto(
    String key,
    Long size,
    String lastModified
) {}
