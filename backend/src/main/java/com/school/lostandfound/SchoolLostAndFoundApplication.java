package com.school.lostandfound;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.school.lostandfound.mapper")
public class SchoolLostAndFoundApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolLostAndFoundApplication.class, args);
    }

}
