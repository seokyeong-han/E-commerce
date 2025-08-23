# E-commerce Backend Project

이 프로젝트는 **대규모 이커머스 서비스의 백엔드 기능을 학습/구현**하기 위해 진행되었습니다.  
쿠폰 발급, 재고 관리, 주문/결제 등 실제 서비스 환경에서 발생할 수 있는 **동시성 제어, 이벤트 기반 처리, 분산 시스템** 이슈를 직접 다루는 것을 목표로 했습니다.

## 🔑 주요 기능
- **회원(User)**
  - 잔액 충전, 포인트 관리 (충전/사용 이력 기록)

- **상품(Product)**
  - 상품 조회, 재고 차감 및 재고 히스토리 관리
    
- **쿠폰(Coupon)**
  - 쿠폰 발급 (선착순/중복 방지)
  - Redisson 기반 분산 락으로 동시성 제어
  - 발급 이력 관리


## 🛠️ 기술 스택

- **Backend:** Spring Boot 3.x, Java 17  
- **DB:** MySQL 8.x, JPA/Hibernate  
- **Cache/Lock:** Redis (Redisson, 분산락)  
- **Infra/Test:** Docker Compose, Testcontainers, JUnit5, Mockito  
- **Documentation:** Swagger / OpenAPI

- ## ⚡ 동시성 제어 전략
- **쿠폰 발급:** Redisson 기반 분산 락
- **재고 차감:** 낙관적 락
- **테스트:** JUnit + Testcontainers로 통합 테스트 수행
