package me.gowoo.studyrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = { "classpath:jdbc.properties" })
public class StudyRestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyRestApiApplication.class, args);
    }
}
