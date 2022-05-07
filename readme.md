### Spring-boot 쇼핑몰 만들기
### 2022-01월 프로젝트

## 📢 프로젝트 설명
- 스프링부트를 이용하여 만든 쇼핑몰 프로젝트입니다.
- 유저 역할을 판매자와 구매자로 나누어 구현하였습니다.
- 판매자
  - 판매자 페이지, 상품 CRUD, 상품관리, 판매 현황 조회
- 구매자
  - 마이페이지, 장바구니, 주문/주문취소, 주문 내역 조회, 코인충전

## ⚙ 개발 환경
- 운영체제 : Windows 10
-통합개발환경(IDE) : IntelliJ
- JDK 버전 : JDK 17
- 데이터 베이스 : MariaDB
- 빌드 툴 : Gradle
- 관리 툴 : GitHub


## 🔌 Dependencies
- Spring Boot DevTools
- Lombok
- Spring Data JPA
- MariaDB Driver
- Spring Security
- Spring Web
- Oauth2-client
- Thymeleaf


## 💻 기술 스택
- 백엔드
  - SpringBoot, Spring Security, Spring Data JPA
- 프론트엔드
  - HTML, CSS, Javascript, Bootstrap, Thymeleaf
- 데이터베이스
  - MariaDB, MySQL Workbench


## 🛠 DB 설계
- User
- Item
- Cart
- CartItem
- Order
- OrderItem
- Sale
- SaleItem


## 🕹 구현 기능
- Entity 설계 (User, Item, Cart, CartItem, Board) (2022-01-14)
- 상품 기능 구현 (CRUD) (2022-01-15)
- 상품 CRUD 관련 html (2022-01-17)
- 상품 리스트 페이지 검색 기능 (2022-01-19)
- 상품 리스트 페이지 페이징 처리 (2022-01-19)
- 판매자/구매자 프로필 페이지 구현 html (2022-01-22)
- 장바구니 기능 구현 (2022-01-22)
- 장바구니 페이지 html (2022-01-22)
- 주문 관련 Entity 추가 설계 (Order, OrderItem) (2022-01-24)
- 판매자/구매자 프로필 페이지와 연관된 회원정보수정 구현 (2022-01-25)
- 장바구니 상품 주문 기능 구현 (2022-01-26)
- 상품관리 페이지, 판매내역 페이지 html (2022-01-26)
- 판매 관련 Entity 추가 설계 (Sale, SaleItem) (2022-01-27)
- 상품 상세 페이지 렌더링 (th:if) (2022-01-28)
- 전체 html 통일 및 완성 (타임리프) (2022-01-28)
- 개별 상품 구매 기능 추가 (2022-01-29)
- 주문 취소 기능 구현 (2022-01-30)
- 기능 별 예외 처리 (2022-01-31)
- 충전 API 추가 (2022-02-01)
- 완성 (2022-02-05)

## 🔗Link

[프로젝트 완성 및 시연 영상](https://velog.io/@rladuswl/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%99%84%EC%84%B1-%EB%B0%8F-%EC%8B%9C%EC%97%B0-%EC%98%81%EC%83%81)
