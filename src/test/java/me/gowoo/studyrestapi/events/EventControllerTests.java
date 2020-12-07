package me.gowoo.studyrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("기본 객체 생성시 성공")
    public void creatEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("rest api practice")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,11,27,17,24))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,11,28,17,33))
                .beginEventDateTime(LocalDateTime.of(2020,11,29,17,53))
                .endEventDateTime(LocalDateTime.of(2020,11,30,17,01))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        /*mock 객체는 null을 return하기 때문에  contoller에서
        * null에 save를 수행해 null Exception이 발생한다. save가 수행할때 event가 들어가게 mocking을 해주자*/
        //Mockito.when(eventRepository.save(event)).thenReturn(event); (@MockBean, @MockMvc)

        mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,"application/hal+json;charset=UTF-8"))
                .andExpect(jsonPath("id").value(Matchers.not(100))) //id,free..와 같은 필드는 입력할 수 없는 값이어야한다.
                .andExpect(jsonPath("free").value(Matchers.not(true))) //id,free..와 같은 필드는 입력할 수 없는 값이어야한다.
        ;
    }

    @Test
    @DisplayName("접근 불가능한 id,price 값 지정하는 경우 에러발생")
    public void creatEventBadRequest() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("rest api practice")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,11,27,17,24))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,11,28,17,33))
                .beginEventDateTime(LocalDateTime.of(2020,11,29,17,53))
                .endEventDateTime(LocalDateTime.of(2020,11,30,17,01))
                .basePrice(100)
                .maxPrice(200)
                .free(true)
                .offline(true)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        /*mock 객체는 null을 return하기 때문에  contoller에서
        * null에 save를 수행해 null Exception이 발생한다. save가 수행할때 event가 들어가게 mocking을 해주자*/
        //Mockito.when(eventRepository.save(event)).thenReturn(event); (@MockBean, @MockMvc)

        mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
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
        Event event = Event.builder()
                .name("Spring")
                .description("rest api practice")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,11,29,17,24))
                .closeEnrollmentDateTime(LocalDateTime.of(2020,11,29,17,33))
                .beginEventDateTime(LocalDateTime.of(2020,11,29,17,53))
                .endEventDateTime(LocalDateTime.of(2020,11,20,17,01))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        /*mock 객체는 null을 return하기 때문에  contoller에서
         * null에 save를 수행해 null Exception이 발생한다. save가 수행할때 event가 들어가게 mocking을 해주자*/
        //Mockito.when(eventRepository.save(event)).thenReturn(event); (@MockBean, @MockMvc)

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
