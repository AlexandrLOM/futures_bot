package com.lom.futures;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FuturesBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuturesBotApplication.class, args);
    }

}
