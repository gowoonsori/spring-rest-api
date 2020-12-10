# Spring 이용한 REST API 예제 공부

## 추가한 dependency
- Web
- JPA
- HATEOAS
- REST Docs
- Lombok
- H2
- PostgreSQL

- Valid (spring 2.3이후부터는 entity 필드값 검증에 사용되는 Valid는 dependency추가해야함)
- JunitParams (파라미터있는 테스트코드 작성을 위해)
- modelMapper (Entity DTO로 mapping)

## 자바 버전
- jdk 11

## build
- maven, jar

<br><Br>

---

## Service
이벤트 등록 서비스

## Entity
|필드 이름|자료형|설명|
|:---:|:---:|:---:|
|id|Integer|id(Auto Increasement)|
name|String|이벤트 등록한 사람 이름
description | String | 이벤트 설명
|beginEnrollmentDateTime|LocalDateTime|이벤트 참가 등록 가능 날짜
closeEnrollmentDateTime|LocalDateTime|이벤트 참가 등록 마감 날짜
beginEventDateTime|LocalDateTime|이벤트 시작날짜
endEventDateTime|LocalDateTime|이벤트 종료 날짜
location|String|장소
basePrice|int|기본 가격
maxPrice|int|최대 가격
limitOfEnrollment|int|등록인원 제한
offline | boolean| 장소에따라 온라인/오프라인
free | boolean | 가격에따라 공짜인지 아닌지
eventStatus | Enum |DRAFT, PUBLISHED, BEGAN_ENROLLMENT ( 초안, 발표, 등록 시작했는지)
