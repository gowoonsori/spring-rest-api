package me.gowoo.studyrestapi.events;

import junitparams.JUnitParamsRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("의성")
                .description("안녕하세요")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        //given
        String name ="Event";
        String description = "Spring";
        //when
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        //then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @ParameterizedTest(name="{index}번째 실행 : basePrice={0},maxPrice={1},isFree={2}")
    @CsvSource({
            "0,0,true",
            "100,0,false",
            "0,100,false"
    })
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        //Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        //When
        event.update();
        //Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    //Type Safe하게 Method이용해서 파리미터 전달
    @ParameterizedTest(name="{index}번째 실행 : location={0}, isOffline ={1}")
    @MethodSource("parametersForTestOffline")
    public void testOffline(String location,boolean isOffline){
        //Given
        Event event = Event.builder()
                .location(location)
                .build();
        //When
        event.update();
        //Then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }
    //static이 있어야 동작한다.
    private static Object[] parametersForTestOffline(){
        return new Object[]{
                new Object[] {"강남역",true},
                new Object[] {"",false},
                new Object[] {"  ",false},
                new Object[] {null,false}
        };
    }
    //외국에서는 Stream을 주로 이용
    private static Stream<Arguments> TestOffline(){
        return Stream.of(
                Arguments.of("강남역",true),
                Arguments.of("",false),
                Arguments.of("  ",false),
                Arguments.of(null,false)
        );
    }
}