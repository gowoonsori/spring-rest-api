package me.gowoo.studyrestapi.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
//Bean Serialize
/*public class EventResource extends RepresentationModel<EventResource> {

    @JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        this.event = event;
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel()); //self link
    }

    public Event getEvent() {
        return event;
    }
}*/
public class EventResource extends EntityModel<Event> {

    public EventResource(Event event, Link... links){
        super(event,links);
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel()); //self link
    }
}
