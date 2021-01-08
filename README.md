# Spring 이용한 REST API 예제 공부

## 추가한 dependency

- Web
- JPA
- HATEOAS
- REST Docs
- Lombok
- H2
- MySQL

- Valid (spring 2.3이후부터는 entity 필드값 검증에 사용되는 Valid는 dependency추가해야함)
- JunitParams (파라미터있는 테스트코드 작성을 위해)
- modelMapper (Entity DTO로 mapping)

plugin

- asciidoctor-maven-plugin (asciidoc파일을 html파일로 바꿔 생성하기 위한 plugin)
- maven-resources-plugin (build하고나서 생긴 docs파일을 static안의 폴더로 옮김으로써, 서버 실행시 url로 접속가능하게 만든다)

## 자바 버전

- jdk 11

## build

- maven, jar

<br><Br>

---

## Service

이벤트 등록 서비스

## Entity

|        필드 이름        |    자료형     |                               설명                                |
| :---------------------: | :-----------: | :---------------------------------------------------------------: |
|           id            |    Integer    |                       id(Auto Increasement)                       |
|          name           |    String     |                      이벤트 등록한 사람 이름                      |
|       description       |    String     |                            이벤트 설명                            |
| beginEnrollmentDateTime | LocalDateTime |                    이벤트 참가 등록 가능 날짜                     |
| closeEnrollmentDateTime | LocalDateTime |                    이벤트 참가 등록 마감 날짜                     |
|   beginEventDateTime    | LocalDateTime |                          이벤트 시작날짜                          |
|    endEventDateTime     | LocalDateTime |                         이벤트 종료 날짜                          |
|        location         |    String     |                               장소                                |
|        basePrice        |      int      |                             기본 가격                             |
|        maxPrice         |      int      |                             최대 가격                             |
|    limitOfEnrollment    |      int      |                           등록인원 제한                           |
|         offline         |    boolean    |                    장소에따라 온라인/오프라인                     |
|          free           |    boolean    |                    가격에따라 공짜인지 아닌지                     |
|       eventStatus       |     Enum      | DRAFT, PUBLISHED, BEGAN_ENROLLMENT ( 초안, 발표, 등록 시작했는지) |

## 파일구조

- main
- asciidoc/index.adoc : REST Docs 생성위한 index.adoc파일
- java
  - common
    - ErrorResource : json형태의 Error메시지에 index링크 추가 하기 위한 파일
    - ErrorSerialize : Errors메시지를 json형태로 Serialize하기 위함
  - events
    - Event : Event 도메인(entity)
    - EventStatus : Event 도메인 필드중 enum값 정의
    - EventDto : 도메인 생성시 직접 생성 못하는 값들을 검증하기 위해 DTO로 입력받고 Event로 변환하기 위한 Dto
    - EventController
    - EventRepository
    - EventResource : Event를 json형태로 바꿔 응답할때 링크도 추가하기 위함
    - EventValidator : @Valid로 기본값 검증외의 custom 검증 정의
  - index
    - IndexController : index페이지로 요청시 다음 event가 들어있는 링크 json데이터 전송위한 로직
  - StudyRestApiApplication : Main Method (EventDtp객체를 Event로 매핑시키기 위한 Model Mapper도 정의)
- resources
  - application.properties
- test
