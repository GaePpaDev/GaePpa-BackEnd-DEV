# GaePpa-BackEnd-DEV
개빠른민족 프로젝트 백엔드 레포지토리 입니다.
---

# 프로젝트 소개

<aside>
💡

배달 및 포장 음식 주문 관리 플랫폼

</aside>

### 프로젝트 개발 목표

음식점들의 배달 및 포장 주문 관리, 결제, 그리고 주문 내역 관리 기능을 제공하는 플랫폼 개발 및 배포

### 주요기능

1. 회원 및 사장님 계정 관리
2. 음식점 카테코리 및 상품 관리
3. 주문 및 리뷰 관리
4. 결제 로그 (PG사 결제 연동은 외주로 진행하여 내역만 저장)
5. 상품 설명 자동 생성 AI

### 팀 구성 및 역할 분담

<aside>
👑 **김성우**
[담당기능]
- Security
- Member/Store
</aside>

😁 **박준형**
[담당기능]
- GlobalConfig
- Product/AI/Image

</aside>

<aside>
😁**연이현**
[담당기능]
- Order/Payment
- Review

</aside>


# 프로젝트 설계

<aside>

## ERD

![스크린샷 2024-11-18 오후 10 47 33](https://github.com/user-attachments/assets/2d2d1790-ad63-4bef-963b-2b3750e3a7ed)


## 기술 스택

| 분류 | 상세 |
| --- | --- |
| IDE | Intellij |
| Language | Java 21 |
| Framework | Spring Boot 3.3.5, Hibernate 6.x, Spring Security, Spring Data JPA |
| Repository | PostgreSQL |
| BuildTool | Gradle |
| Infra | Github, AWS EC2, Docker, AWS RDS |
| Library | Spring Scheduler, Spring WebFlux, Spring Validation, QueryDSL, ElasticSearch, Filebeat, Kibana |
| API 문서화 | Swagger (SpringDoc OpenAPI) |
| 인증/보안 | Spring Security, JWT (io.jsonwebtoken) |
| 테스트 | JUnit, Spring Security Test |


# Gaeppa API 문서

**버전:** 1.0.0

---

## 인증 및 권한
대부분의 엔드포인트는 `storeId`와 `memberId`의 JWT 토큰을 통해 인증됩니다.

---

## 엔드포인트 정리

### 스토어 관리
| 기능               | 엔드포인트                     | 메서드 | 파라미터                           | 응답      |
| ------------------ | ----------------------------- | ------ | ---------------------------------- | --------- |
| 스토어 생성         | `/v1/stores`                 | POST   | 없음                              | 200 OK    |
| 스토어 상세 정보     | `/v1/stores/{storeId}`       | GET    | `storeId` (UUID, 필수)            | 200 OK    |
| 스토어 회원 관리     | `/v1/stores/{storeId}/{memberId}` | GET, PUT, POST | `storeId`, `memberId` (UUID, 필수) | 200 OK    |

---

### 상품 카테고리
| 기능               | 엔드포인트                            | 메서드 | 파라미터                                | 응답      |
| ------------------ | ------------------------------------ | ------ | --------------------------------------- | --------- |
| 카테고리 생성       | `/v1/product-categories`             | POST   | 없음                                   | 200 OK    |
| 카테고리 수정       | `/v1/product-categories/{productCategoryId}` | PUT    | `productCategoryId` (UUID, 필수)       | 200 OK    |
| 카테고리 삭제       | `/v1/product-categories/{productCategoryId}` | DELETE | `productCategoryId` (UUID, 필수)       | 200 OK    |

---

### 상품
| 기능               | 엔드포인트                                   | 메서드 | 파라미터                             | 응답      |
| ------------------ | ------------------------------------------- | ------ | ------------------------------------ | --------- |
| 상품 목록 조회      | `/v1/product-categories/products`          | GET    | `storeId` (UUID, 필수)              | 200 OK    |
| 상품 생성          | `/v1/product-categories/products`          | POST   | 없음                                | 200 OK    |
| 상품 수정          | `/v1/product-categories/products/{productId}` | PUT    | `productId` (UUID, 필수)            | 200 OK    |
| 상품 삭제          | `/v1/product-categories/products/{productId}` | DELETE | `productId` (UUID, 필수)            | 200 OK    |

---

### 상품 옵션
| 기능               | 엔드포인트                                   | 메서드 | 파라미터                             | 응답      |
| ------------------ | ------------------------------------------- | ------ | ------------------------------------ | --------- |
| 옵션 생성          | `/v1/product-categories/products/options`   | POST   | 없음                                | 200 OK    |
| 옵션 수정          | `/v1/product-categories/products/options/{optionId}` | PUT    | `optionId` (UUID, 필수)             | 200 OK    |
| 옵션 삭제          | `/v1/product-categories/products/options/{optionId}` | DELETE | `optionId` (UUID, 필수)             | 200 OK    |

---

### 상품 검색
| 기능               | 엔드포인트                             | 메서드 | 파라미터                                | 응답      |
| ------------------ | ------------------------------------- | ------ | --------------------------------------- | --------- |
| 상품 검색          | `/v1/product-categories/products/search` | GET    | 검색 쿼리 (옵션: `productName`, `categoryName` 등) | 200 OK    |

---

### 리뷰
| 기능               | 엔드포인트               | 메서드 | 파라미터                          | 응답      |
| ------------------ | ----------------------- | ------ | --------------------------------- | --------- |
| 리뷰 목록 조회      | `/v1/review`           | GET    | `storeId` (UUID, 필수)           | 200 OK    |
| 리뷰 생성          | `/v1/review`           | POST   | 없음                             | 200 OK    |
| 리뷰 수정          | `/v1/review`           | PATCH  | `reviewId` (UUID, 필수)          | 200 OK    |

---

### 결제
| 기능               | 엔드포인트               | 메서드 | 파라미터                          | 응답      |
| ------------------ | ----------------------- | ------ | --------------------------------- | --------- |
| 결제 상세 정보 조회 | `/v1/payments`         | GET    | `orderId` (UUID, 필수)           | 200 OK    |
| 결제 생성          | `/v1/payments`         | POST   | 없음                             | 200 OK    |
| 결제 수정          | `/v1/payments`         | PATCH  | `payId` (UUID, 필수)             | 200 OK    |

---

### 주문
| 기능               | 엔드포인트               | 메서드 | 파라미터                          | 응답      |
| ------------------ | ----------------------- | ------ | --------------------------------- | --------- |
| 주문 목록 조회      | `/v1/orders`           | GET    | 없음                             | 200 OK    |
| 주문 생성          | `/v1/orders`           | POST   | 없음                             | 200 OK    |
| 주문 수정          | `/v1/orders`           | PATCH  | `orderId` (UUID, 필수)           | 200 OK    |

---

### 사용자 관리
| 기능               | 엔드포인트               | 메서드 | 파라미터                          | 응답      |
| ------------------ | ----------------------- | ------ | --------------------------------- | --------- |
| 사용자 등록         | `/v1/members/join`     | POST   | 없음                             | 200 OK    |
| 사용자 로그아웃      | `/v1/members/logout`   | POST   | 선택: `refreshAuthorization` (쿠키) | 200 OK    |

---

### 이미지 관리
| 기능               | 엔드포인트                     | 메서드 | 파라미터                          | 응답      |
| ------------------ | ----------------------------- | ------ | --------------------------------- | --------- |
| 리뷰 이미지 업로드  | `/v1/images/review-images`   | POST   | `reviewId` (문자열, 필수)         | 200 OK    |
| 상품 이미지 업로드  | `/v1/images/product-images`  | POST   | `productId` (문자열, 필수)        | 200 OK    |

---

### AI 기능
| 기능               | 엔드포인트                     | 메서드 | 파라미터                          | 응답      |
| ------------------ | ----------------------------- | ------ | --------------------------------- | --------- |
| 시스템 프롬프트 얻기 | `/v1/ai/system-prompt`       | GET, POST | 없음                             | 200 OK    |
| 상품 설명 생성      | `/v1/ai/assist/product-description` | POST   | 없음                             | 200 OK    |

---



# 어려웠던 점

---

### 백엔드와 프론트 동시 진행의 요구사항 정의서

정확하지 않은 요구사항 명세서와 클라이언트 없는 진행에 많은 혼란이 있었다. 다양한 레퍼런스와 튜터님의 피드백을 기준으로 현재 상황에 맞는 로직을 선택해야했다.

동시에 프로젝트의 확장성을 고려해야하는 부분에서 어려움을 느꼈다.

![가장 많이 혼란스러웠던, 요구사항 명세서에 정의된 데이터 삭제](https://github.com/user-attachments/assets/4e074e5c-8ec1-4d50-8672-147092ee16fc)


가장 많이 혼란스러웠던, 요구사항 명세서에 정의된 데이터 삭제

### 철저한 컨벤션 계획 및 개발 방법 설계 [ 김성우 ]

처음 적용해보는 벤션 계획 및 개발 방법 규칙을 정하고, 실천하다보니 

시행착오로 인해 평상시보다 시간 소요가 있었음. 작성하고 나니, 다른 사람과 다른 코드들도 발견되고, 그랬으나 지나고보니 많이 늘었다.

### 협업과 머지 규칙: 소통과 피드백을 통한 코드 품질 향상

협업 과정에서 강력한 머지 규칙을 적용하여 PR을 통해서만 머지가 가능했지만, 서로의 코드를 읽고 이해하며 피드백을 주고받는 데 많은 시간과 노력이 들었다. 그럼에도 불구하고 이러한 과정을 거치면서 코드 품질을 높이고 팀원 간의 소통이 더욱 원활해지는 긍정적인 결과를 얻을 수 있었다.

![화면 캡처 2024-11-18 231956 (1)](https://github.com/user-attachments/assets/2b3821c4-423e-4166-85f1-705321abe17a)

# 잘 된 점

---

### 협업을 위한 철저한 컨벤션 계획 및 개발 방법 설계

프로젝트의 완성도를 높이기 위해 개발 초기 단계에서 컨벤션과 개발 방식을 설계하는 데 많은 시간을 투자하였다. 서로 다른 경험과 관점을 기반으로 공통된 컨벤션을 정립하는 과정에서 어려움도 있었지만, 이를 성공적으로 조율하며 팀의 목표를 구체화할 수 있었다.

결과적으로 초기 계획의 80% 이상이 일정에 맞게 진행되었고 안정적이고 일관된 개발 환경을 구축할 수 있었다.

<img width="1024" alt="github convention" src="https://github.com/user-attachments/assets/c74a6763-eed2-4482-832c-1ac9bd01cfc6">

![image (2)](https://github.com/user-attachments/assets/b493ddd9-f099-490b-8415-8d27552cee51)



### 로그인 인증 및 인가에 대한 설계



LoginFilter 과 JWTFilter 를 정의하였고, 1차적으로 로그인 경로로 들어온 경우 LoginFilter 를 거치고 인증이 된 경우에는 JWTFilter 에서 예외로직으로 빠져서 JWTFilter 에서의 2중 인증을 막았습니다. 

이후 소셜 로그인과 일반 로그인의 통합과 email 필드를 불변성 필드로 두어서 확장성을 고려했습니다. 


# 협업 트러블

---

### 설계에 대한 의견 차이

서로의 경험과 지식이 달라 의견이 엇갈리는 경우도 있었지만 PR과 토론을 통해 점차 옳은 방향을 찾아갔다. 추가 학습과 멘토님의 조언을 바탕으로 팀원들과 함께 최적의 설계 방안을 확립하며, 모두가 만족할 수 있는 결과를 도출했다.

![image (3)](https://github.com/user-attachments/assets/47ade327-6776-4810-96c4-11178798460a)
Orders 엔터티 내 totalPrice 계산 메서드 존재에 대한 코드리뷰 

### 온라인 작업의 한계

온라인 작업에서는 실시간으로 진행 상황을 확인하기 어려워 문제 발생 시 도움을 청하기보다는 혼자 해결하는 경향이 있었습니다. 이로 인해 소통이 어려워졌고, 협업을 위해선 서로의 상황과 문제를 더 명확히 공유하고 설명하는 능력이 필요하다고 느꼈습니다.
![image (4)](https://github.com/user-attachments/assets/8cd9fb80-8dc1-4563-a01e-8a4bb0a14d15)


# 후기

<aside>
🗣

**김성우**

그전까지는 컨벤션과 글로벌 규칙에 대한 구체적인 방안이나 설계 없이 팀프로젝트를 해보다가, 처음으로 서로서로 규칙과 컨벤션을 지켜가며, API 를 Restful 하고 레이어층을 지켜가며 안전하게 설계 해보았다는 것 자체가 의미가 있습니다.

</aside>

<aside>
🗣

**박준형**

적은 인원으로 끝까지 해내서 좋았습니다. 기본적인 기능을 끝내기까지 굉장히 어려웠지만 좋은코드를 잘 짜기위해서 같이 고민해고 토론했던 기간덕분에 고통스럽지만 많은 성장을 할 수 있는 시간이었습니다. 끝까지 함께해서 영광입니다. 수고하셨습니다!

</aside>

<aside>
🗣

**연이현**

Restful 한 API 설계를 경험할 수 있었다. 컨벤션과 글로벌 규칙을 지켜서 완성도 높은 프로젝트를 처음으로 목표해 보았고 만족스러운 결과를 만들 수 있었던 것 같다. 다른 경험의 팀원들을 만나서 새롭게 배운 점이 많았다.

</aside>

---

