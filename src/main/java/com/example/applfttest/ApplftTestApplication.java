package com.example.applfttest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApplftTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplftTestApplication.class, args);
    }

}
