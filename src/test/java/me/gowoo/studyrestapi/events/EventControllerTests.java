package me.gowoo.studyrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.gowoo.studyrestapi.common.RestDocsConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

    @Test
    @DisplayName("기본 객체 생성시 성공")
    public void creatEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("rest api practice")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,10,28,17,24))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,11,28,17,33))
                .beginEventDateTime(LocalDateTime.of(2020,11,20,17,53))
                .endEventDateTime(LocalDateTime.of(2020,12,20,17,1))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        /*mock 객체는 null을 return하기 때문에  contoller에서
        * null에 save를 수행해 null Exception이 발생한다. save가 수행할때 event가 들어가게 mocking을 해주자*/
        //Mockito.when(eventRepository.save(event)).thenReturn(event); (@MockBean, @MockMvc)

        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(this.objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,"application/hal+json;charset=UTF-8"))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                             linkWithRel("self").description("link to self"),
                             linkWithRel("query-events").description("link to query-events"),
                             linkWithRel("update-event").description("link to update-event"),
                             linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                            fieldWithPath("name").description("Name of Event"),
                            fieldWithPath("description").description("Description of Event"),
                            fieldWithPath("beginEnrollmentDateTime").description("등록시작기간 of Event"),
                            fieldWithPath("closeEnrollmentDateTime").description("등록마감기간 of Event"),
                            fieldWithPath("beginEventDateTime").description("시작기간 of Event"),
                            fieldWithPath("endEventDateTime").description("종료기간 of Event"),
                            fieldWithPath("location").description("location of Event"),
                            fieldWithPath("basePrice").description("BasePrice of Event"),
                            fieldWithPath("maxPrice").description("MaxPrice of Event"),
                            fieldWithPath("limitOfEnrollment").description("등록 제한 of Event")
                        )
                        ,responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of Event"),
                                fieldWithPath("name").description("Name of Event"),
                                fieldWithPath("description").description("Description of Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("등록시작기간 of Event"),
                                fieldWithPath("closeEnrollmentDateTime").description("등록마감기간 of Event"),
                                fieldWithPath("beginEventDateTime").description("시작기간 of Event"),
                                fieldWithPath("endEventDateTime").description("종료기간 of Event"),
                                fieldWithPath("location").description("location of Event"),
                                fieldWithPath("basePrice").description("BasePrice of Event"),
                                fieldWithPath("maxPrice").description("MaxPrice of Event"),
                                fieldWithPath("limitOfEnrollment").description("등록 제한 of Event"),
                                fieldWithPath("offline").description("오프라인인지 of Event"),
                                fieldWithPath("free").description("무료인지 of Event"),
                                fieldWithPath("eventStatus").description("eventStatus of Event"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query-events"),
                                fieldWithPath("_links.update-event.href").description("link to update-event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                        ));
    }

    @Test
    @DisplayName("접근 불가능한 id,free등 파라미터 지정하는 경우 에러발생")
    public void creatEventBadRequest() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("rest api practice")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,10,28,17,24))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,11,28,17,33))
                .beginEventDateTime(LocalDateTime.of(2020,11,20,17,53))
                .endEventDateTime(LocalDateTime.of(2020,12,20,17,1))
                .basePrice(100)
                .maxPrice(200)
                .free(true)
                .offline(true)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(this.objectMapper.writeValueAsString(event)))
                    .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("필수 값이 없는 객체 생성할때 에러발생")
    public void 이벤트생성_bad_Request_값이_없을때() throws Exception{
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                    .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("입력값의 잘못된 경우(조건값이 다른 경우) 에러 발생")
    public void creatEventBadRequest_wrong_input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("rest api practice")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,10,28,17,24))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,11,28,17,33))
                .beginEventDateTime(LocalDateTime.of(2020,10,25,17,53))
                .endEventDateTime(LocalDateTime.of(2020,12,20,17,1))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception{
        //Given
        IntStream.range(0,30).forEach(i -> {
            this.generateEvent(i);
        });

        //When
        this.mockMvc.perform(get("/api/events")
                    .param("page","1")
                    .param("size","10")
                    .param("sort","id,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("page").exists())
                .andDo(document("query-events",
                        links(
                                linkWithRel("first").description("첫번째 페이지로 이동하는 링크"),
                                linkWithRel("prev").description("이전 페이지로 이동하는 링크"),
                                linkWithRel("self").description("현재 페이지의 링크"),
                                linkWithRel("next").description("다음 페이지로 이동하는 링크"),
                                linkWithRel("last").description("마지막 페이지로 이동하는 링크"),
                                linkWithRel("profile").description("api 문서의 링크")
                        ),
                        requestParameters(
                                parameterWithName("page").description("요청 페이지 번호로 0부터 시작해서 1이면 2번째 페이지"),
                                parameterWithName("size").description("한 페이지의 데이터 개수"),
                                parameterWithName("sort").description("정렬할 field와 오름,내림차순으로 DESC는 내림차순")
                        )
                        ,responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.eventList[].id").description("identifier of Event"),
                                fieldWithPath("_embedded.eventList[].name").description("Name of Event"),
                                fieldWithPath("_embedded.eventList[].description").description("Description of Event"),
                                fieldWithPath("_embedded.eventList[].beginEnrollmentDateTime").description("등록시작기간 of Event"),
                                fieldWithPath("_embedded.eventList[].closeEnrollmentDateTime").description("등록마감기간 of Event"),
                                fieldWithPath("_embedded.eventList[].beginEventDateTime").description("시작기간 of Event"),
                                fieldWithPath("_embedded.eventList[].endEventDateTime").description("종료기간 of Event"),
                                fieldWithPath("_embedded.eventList[].location").description("location of Event"),
                                fieldWithPath("_embedded.eventList[].basePrice").description("BasePrice of Event"),
                                fieldWithPath("_embedded.eventList[].maxPrice").description("MaxPrice of Event"),
                                fieldWithPath("_embedded.eventList[].limitOfEnrollment").description("등록 제한 of Event"),
                                fieldWithPath("_embedded.eventList[].offline").description("오프라인인지 of Event"),
                                fieldWithPath("_embedded.eventList[].free").description("무료인지 of Event"),
                                fieldWithPath("_embedded.eventList[].eventStatus").description("eventStatus of Event"),
                                fieldWithPath("_embedded.eventList[]._links.self.href").description("link to self"),
                                fieldWithPath("_links.first.href").description("첫번째 페이지로 이동하는 링크"),
                                fieldWithPath("_links.prev.href").description("이전 페이지로 이동하는 링크"),
                                fieldWithPath("_links.self.href").description("현재 페이지의 링크"),
                                fieldWithPath("_links.next.href").description("다음 페이지로 이동하는 링크"),
                                fieldWithPath("_links.last.href").description("마지막 페이지로 이동하는 링크"),
                                fieldWithPath("_links.profile.href").description("api 문서의 링크"),
                                fieldWithPath("page.size").description("한 페이지에 불러오는 데이터 개수"),
                                fieldWithPath("page.totalElements").description("총 이벤트 데이터 개수"),
                                fieldWithPath("page.totalPages").description("총 페이지 개수"),
                                fieldWithPath("page.number").description("현재 페이지 수로 부터 시작")
                        )));
    }

    private void generateEvent(int i) {
        Event event = Event.builder()
                .name("event" + i)
                .description("test event" + i)
                .build();

        this.eventRepository.save(event);
    }
}
