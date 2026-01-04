# Lombok Annotations and DTO Conversion Explanation

## @ToString Annotation

### 역할
- Lombok 라이브러리의 애노테이션으로, `toString()` 메서드를 자동 생성합니다.
- 객체를 문자열로 표현할 때 모든 필드 값을 표시합니다.

**예시**:
```java
@ToString
public class ProductHistory {
    private String userId;
    private String viewTime;
    // ...
}
// 생성되는 toString(): "ProductHistory(userId=user123, viewTime=2025-01-04, ...)"
```

### 왜 컴파일 시점에 생성?
- 보일러플레이트 코드 제거: 수동 작성 불필요.
- 유지보수성 향상: 필드 추가 시 자동 반영.
- 런타임 성능: 리플렉션 없이 바이트코드로 생성.
- 일관성 보장: 모든 필드 자동 포함.

### Entity에 쓰는 이유
- **디버깅**: 객체 상태 빠르게 확인.
- **로깅**: 서버 로그에 데이터 기록.
- **API 응답 확인**: 반환 데이터 검증.
- **데이터 무결성**: 저장 전/후 상태 확인.

**@ToString 없을 때 vs 있을 때**:
- 없음: `com.example.dynamodb.entity.ProductHistory@4aa298b7`
- 있음: `ProductHistory(userId=user123, viewTime=2025-01-04, ...)`

### toString() 기본 개념
- Java 객체의 문자열 표현 메서드.
- `System.out.println(객체)`, 문자열 연결 시 자동 호출.
- 기본 구현: `클래스명@해시코드` (정보 부족).

## DTO (Data Transfer Object)

### 왜 Entity → DTO 변환?
- **계층 분리**: DB 스키마와 API 계약 분리.
- **보안**: 필요한 필드만 노출, 민감 데이터 숨김.
- **응답 맞춤화**: 필드 선택, 포맷 변경 가능.
- **검증/변환**: 입력 검증 후 엔티티 매핑.
- **버전 관리**: API 변경 시 DTO만 조정.
- **지연 로딩 방지**: 순환 참조, Lazy 로딩 문제 해결.

## @Getter Annotation

### 역할
- Lombok이 getter 메서드(`getUserId()`, `getViewTime()` 등)를 자동 생성.
- 필드 접근을 위한 메서드 제공.

### JSON 직렬화와의 관계
- 스프링 MVC + Jackson이 getter를 사용해 JSON 변환.
- 컨트롤러 반환 시 자동으로 JSON 생성.
- `@Getter` 없으면 Jackson이 필드 읽지 못해 JSON 실패.

**직접 호출 vs 자동 호출**:
- 직접: 코드에서 `dto.getUserId()` 명시적 사용.
- 자동: JSON 변환, 로깅 시 프레임워크가 호출.

### Service 레이어 활용 (31-35라인)
```java
public List<ProductHistoryResponse> getUserHistories(String userId) {
    return repository.findByUserId(userId, false)  // DB 조회
         .stream()                                  // 스트림 변환
         .map(ProductHistoryResponse::new)          // Entity → DTO 변환
         .collect(Collectors.toList());             // 리스트 수집
}
```
- **DB 조회**: `List<ProductHistory>` 가져옴.
- **스트림 변환**: 함수형 처리 준비.
- **매핑**: 각 엔티티를 DTO로 변환 (보안/유연성).
- **수집**: `List<ProductHistoryResponse>` 반환.

### Controller 활용
- 서비스에서 DTO 리스트 반환.
- 컨트롤러가 그대로 반환 → Jackson이 `@Getter`로 JSON 변환.

## @Getter vs entity.getUserId()

### 차이점
| 항목 | @Getter | entity.getUserId() |
|------|---------|-------------------|
| 타입 | 애노테이션 (코드 생성) | 메서드 호출 (실행) |
| 적용 | 클래스 전체 | 특정 객체 |
| 시점 | 컴파일 시 | 런타임 |
| 목적 | 필드 접근 허용 | 데이터 복사 |

### entity.getUserId()의 역할
- DTO 생성자에서 엔티티 데이터를 DTO로 복사.
- 엔티티 필드가 private이므로 getter로 접근.
- JSON 직렬화와 무관 (데이터 전송 단계).

**흐름**:
1. DB 조회 → 엔티티 리스트.
2. `new ProductHistoryResponse(entity)` → 데이터 복사.
3. 컨트롤러 반환 → JSON 변환.

## 결론
- `@ToString`: 디버깅/로깅용, 프로그램 기능에 영향 없음.
- `@Getter`: JSON 직렬화 필수, 외부 접근 허용.
- `entity.getUserId()`: 엔티티 → DTO 데이터 복사.
- DTO 변환: 보안, 유연성, 계층 분리를 위해 필수.</content>
<parameter name="filePath">c:\Users\hwplu\Documents\mzc\aws-sdk\dynamodb-denormalization-practice\annotations_explanation.md