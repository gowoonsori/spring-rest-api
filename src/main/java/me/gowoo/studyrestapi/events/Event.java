package me.gowoo.studyrestapi.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime; //등록기간
    private LocalDateTime closeEnrollmentDateTime; //등록마감
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; //(optional) 이게 없으면 온라인 모임
    private int basePrice; //(optional)
    private int maxPrice; //(optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    public void update() {
        //Update Free
        if (this.basePrice == 0 && this.maxPrice == 0){
            this.free=true;
        }else{
            this.free = false;
        }

        //Update offline()
        //isBlank() => 스페이스 포함 모든 공백 문자열 포함해서 비어있는지 검사
        if(this.location == null || this.location.isBlank()){
            this.offline=false;
        }else{
            this.offline=true;
        }
    }
}
