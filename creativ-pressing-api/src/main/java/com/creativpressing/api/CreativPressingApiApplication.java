package com.creativpressing.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CreativPressingApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CreativPressingApiApplication.class, args);
    }
}
