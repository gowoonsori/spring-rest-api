package me.gowoo.studyrestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors){

        //price
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0){
            errors.rejectValue("basePrice","wrongBasePrice","BasePrice is wrong");
            errors.rejectValue("maxPrice","wrongMaxPrice","maxPrice is wrong");
        }

        //이벤트 끝나는 기간이 다른 날짜들보다 빠르다면 error
        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
            endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            errors.rejectValue("EndEventDateTime","wrongEndEventDateTime","EndEventDate time is wrong");
        }

       //이벤트 시작기간이 등록시작날짜보다 빠르거나 마감날짜/끝나는 날짜보다 늦다면 error
        LocalDateTime beginEventDateTime = eventDto.getBeginEventDateTime();
        if(beginEventDateTime.isAfter(eventDto.getEndEventDateTime()) ||
                beginEventDateTime.isAfter(eventDto.getCloseEnrollmentDateTime()) ||
                beginEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            errors.rejectValue("BeginEventDateTime","wrongBeginEventDateTime","BeginEventDate time is wrong");
        }

        //등록 시작 시간이 다른 기간들보다 늦다면 error
        LocalDateTime beginEnrollmentDateTime = eventDto.getBeginEnrollmentDateTime();
        if(beginEnrollmentDateTime.isAfter(eventDto.getEndEventDateTime()) ||
                beginEnrollmentDateTime.isAfter(eventDto.getBeginEventDateTime()) ||
                beginEnrollmentDateTime.isAfter(eventDto.getCloseEnrollmentDateTime())){
            errors.rejectValue("BeginEnrollmentDateTime","wrongBeginEnrollmentDateTime","BeginEnrollmentDate time is wrong");
        }

        //등록마감 날짜가 등록시작 날짜보다 빠르거나 이벤트 시작날짜보다 빠르거나 이벤트 끝나는 날짜보다 늦다면 error
        LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
        if(closeEnrollmentDateTime.isAfter(eventDto.getEndEventDateTime()) ||
                closeEnrollmentDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
                closeEnrollmentDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            errors.rejectValue("CloseEnrollmentDateTime","wrongEndEnrollmentDateTime","CloseEnrollmentDate time is wrong");
        }

    }

}
