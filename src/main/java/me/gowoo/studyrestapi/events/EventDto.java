package me.gowoo.studyrestapi.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/*JackSon json이 제공하는 어노테이션을 사용해서 입력값 검증을 할 수 있는데, Entity안의
 * 어노테이션이 너무 많아 질 수 있어 가독성을 위해 따로 DTO로 분리*/
/*입력값을 받는 DTO
* 단점으로는 Entity의 필드 중복이 발생*/

@Builder  @NoArgsConstructor @AllArgsConstructor @Data
public class EventDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private LocalDateTime beginEnrollmentDateTime;
    @NotNull
    private LocalDateTime closeEnrollmentDateTime;
    @NotNull
    private LocalDateTime beginEventDateTime;
    @NotNull
    private LocalDateTime endEventDateTime;
    private String location; //(optional) 이게 없으면 온라인 모임
    @Min(0)
    private int basePrice; //(optional)
    @Min(0)
    private int maxPrice; //(optional)
    @Min(0)
    private int limitOfEnrollment;
}
