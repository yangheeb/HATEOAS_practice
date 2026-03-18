# 🛒 HATEOAS 기반 상품/주문 API

Spring Boot와 HATEOAS를 활용한 RESTful API 실습 프로젝트입니다.
상품 등록/조회/수정/삭제와 주문 기능을 제공하며, JWT 기반 인증을 적용했습니다.

---

## 🛠 기술 스택

| 분류 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.3 |
| ORM | Spring Data JPA |
| 인증 | Spring Security + JWT (jjwt 0.11.5) |
| API 문서 | springdoc-openapi (Swagger UI) |
| HATEOAS | Spring HATEOAS |
| DB | MySQL |
| 빌드 | Gradle |

---

## 📦 패키지 구조

```
src/main/java/dev/fisa/hateoas_practice/
├── auth/
│   ├── AuthController.java       # 회원가입 / 로그인 API
│   ├── AuthService.java          # 인증 비즈니스 로직
│   ├── LoginRequest.java
│   └── SignupRequest.java
├── controller/
│   ├── ProductController.java    # 상품 CRUD API
│   └── OrderController.java     # 주문 API
├── dto/
│   ├── ProductCreateRequest.java
│   ├── ProductUpdateRequest.java
│   ├── ProductResponse.java
│   ├── ProductModel.java         # HATEOAS 링크 포함 상품 응답
│   ├── OrderCreateRequest.java
│   ├── OrderResponse.java
│   └── OrderModel.java           # HATEOAS 링크 포함 주문 응답
├── model/
│   ├── User.java
│   ├── Product.java
│   ├── Order.java
│   └── OrderStatus.java
├── repository/
│   ├── UserRepository.java
│   ├── ProductRepository.java
│   └── OrderRepository.java
├── security/
│   ├── JwtTokenProvider.java     # 토큰 생성 / 검증
│   ├── JwtAuthenticationFilter.java # 요청마다 토큰 파싱
│   └── SecurityConfig.java       # 필터 체인 설정
└── service/
    ├── ProductService.java
    └── OrderService.java
```

---

## ▶️ 실행 방법

### 1. MySQL 데이터베이스 생성

```sql
CREATE DATABASE hateoas_db;
```

### 2. 환경 설정 파일 생성

`src/main/resources/application.properties.example`을 복사해 `application.properties`를 만들고 본인 환경에 맞게 수정하세요.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hateoas_db?serverTimezone=UTC&characterEncoding=UTF-8
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

jwt.secret=your_secret_key
jwt.expiration=86400000

springdoc.swagger-ui.path=/swagger-ui/index.html
springdoc.api-docs.path=/v3/api-docs
```

### 3. 서버 실행

```bash
./gradlew bootRun
```

### 4. Swagger UI 접속

```
http://localhost:8080/swagger-ui/index.html
```

---

## 🔐 인증 방법

1. `POST /api/auth/signup` 으로 회원가입
2. `POST /api/auth/login` 으로 로그인 후 `accessToken` 복사
3. Swagger 상단 **Authorize 🔒** 버튼 클릭
4. `Bearer {accessToken}` 형식으로 입력 후 Authorize

---

## 📡 API 엔드포인트

### 인증

| 메서드 | URI | 설명 | 인증 필요 |
|---|---|---|---|
| POST | `/api/auth/signup` | 회원가입 | ❌ |
| POST | `/api/auth/login` | 로그인 (토큰 발급) | ❌ |

### 상품

| 메서드 | URI | 설명 | 인증 필요 |
|---|---|---|---|
| POST | `/api/products` | 상품 등록 | ✅ |
| GET | `/api/products` | 전체 상품 조회 (필터링, 페이징) | ❌ |
| GET | `/api/products/{id}` | 상품 상세 조회 | ❌ |
| PUT | `/api/products/{id}` | 상품 수정 (본인 상품만) | ✅ |
| DELETE | `/api/products/{id}` | 상품 삭제 (본인 상품만) | ✅ |

### 주문

| 메서드 | URI | 설명 | 인증 필요 |
|---|---|---|---|
| POST | `/api/orders` | 상품 주문 | ✅ |
| GET | `/api/orders/{id}` | 주문 내역 조회 (본인 주문만) | ✅ |

---

## 🔗 HATEOAS 링크 예시

상품 등록 후 응답에 `_links`가 포함됩니다.

```json
{
  "id": 1,
  "name": "맥북 pro",
  "price": 3500000,
  "stock": 5,
  "category": "전자제품",
  "_links": {
    "self": { "href": "http://localhost:8080/api/products/1" },
    "profile": { "href": "/swagger-ui/index.html" },
    "list-products": { "href": "/api/products?page=0&size=10{&category}", "templated": true },
    "update-product": { "href": "http://localhost:8080/api/products/1", "type": "PUT" },
    "delete-product": { "href": "http://localhost:8080/api/products/1", "type": "DELETE" }
  }
}
```

---

## 📋 응답 코드

| 코드 | 의미 | 상황 |
|---|---|---|
| 200 | OK | 조회 / 수정 성공 |
| 201 | Created | 상품 등록 / 주문 성공 |
| 400 | Bad Request | 유효성 실패 / 재고 부족 |
| 401 | Unauthorized | JWT 누락 또는 유효하지 않음 |
| 403 | Forbidden | 타인의 리소스 접근 시도 |
| 404 | Not Found | 리소스가 존재하지 않음 |
