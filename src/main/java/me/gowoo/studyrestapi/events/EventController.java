package me.gowoo.studyrestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = "application/hal+json; charset=UTF-8")
public class EventController {
    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidater;

    public EventController(EventRepository eventRepository,ModelMapper modelMapper,EventValidator eventValidater) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidater = eventValidater;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){
//        Event event = Event.builder()
//                .name(eventDto.getName())
//                .description(eventDto.getDescription())
//                ..
//          위 와 같은 Dto를 Entity에 맵핑해줘야 하는데 너무 귀찮다... 따라서 ModelMapper를 사용
//          Reflaction이 발생해 직접 맵핑하는 것보단 속도가 느려질 수 있다.

        //Bean Validator 이용
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        //Custom Validator이용
        eventValidater.validate(eventDto,errors);
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto,Event.class);
        event.update();
        Event newEvent = this.eventRepository.save(event);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();

        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(new Link("/docs/index.html#resources-events#resources-events-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(eventResource);
    }
}
