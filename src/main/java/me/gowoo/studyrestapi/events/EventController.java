package me.gowoo.studyrestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = "application/hal+json; charset=UTF-8")
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    public EventController(EventRepository eventRepository,ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody EventDto eventDto){
//        Event event = Event.builder()
//                .name(eventDto.getName())
//                .description(eventDto.getDescription())
//                ..
//          위 와 같은 Dto를 Entity에 맵핑해줘야 하는데 너무 귀찮다... 따라서 ModelMapper를 사용
//          Reflaction이 발생해 직접 맵핑하는 것보단 속도가 느려질 수 있다.
        Event event = modelMapper.map(eventDto,Event.class);
        Event newEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();

        return ResponseEntity.created(createdUri).body(event);
    }
}
