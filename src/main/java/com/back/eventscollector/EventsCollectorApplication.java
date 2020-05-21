package com.back.eventscollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EventsCollectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventsCollectorApplication.class, args);
    }
}