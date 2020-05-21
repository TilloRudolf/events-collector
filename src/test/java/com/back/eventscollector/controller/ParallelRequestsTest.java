package com.back.eventscollector.controller;

import com.back.eventscollector.model.Event;
import com.back.eventscollector.model.EventsCount;
import com.back.eventscollector.model.HandleEventResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ParallelRequestsTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    public void parallelTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Runnable> tasks = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String taskName = "name-" + i;
            tasks.add(() -> {
                for (int j = 0; j < 10; j++) {
                    String eventName = taskName + "-" + j;
                    handleEvent(eventName);
                }
            });
        }

        CompletableFuture[] futures = tasks.stream()
                .map(task -> CompletableFuture.runAsync(task, executorService))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();

        long eventsCount = minuteEvents().getEventsCount();
        assertThat(eventsCount).isEqualTo(100);
    }

    private void handleEvent(String name) {
        restTemplate.postForEntity(
                "http://localhost:" + port + "/events",
                new Event(name, "description", System.currentTimeMillis()),
                HandleEventResponse.class);
    }

    private EventsCount minuteEvents() {
        ResponseEntity<EventsCount> forEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/events/minute",
                EventsCount.class);
        return forEntity.getBody();
    }
}
