package com.back.eventscollector.controller;

import com.back.eventscollector.model.Event;
import com.back.eventscollector.model.EventsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestsTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getShouldReturnOK() {
        ResponseEntity<EventsResponse> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/events/period?range=minute", EventsResponse.class);
        assertThat(forEntity);
        assertThat(forEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(Objects.requireNonNull(forEntity.getBody()).getEvents()).isEmpty();
    }

    @Test
    public void getShouldReturnMETHOD_NOT_ALLOWED() {
        ResponseEntity<EventsResponse> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/events/period?range=m", EventsResponse.class);
        assertThat(forEntity);
        assertThat(forEntity.getStatusCodeValue()).isEqualTo(405);
    }

    @Test
    public void getShouldReturnBAD_REQUEST() {
        ResponseEntity<EventsResponse> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/events/period", EventsResponse.class);
        assertThat(forEntity);
        assertThat(forEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void addEvent() {
        restTemplate.put("http://localhost:" + port + "/events/event", new Event("name", "description", System.currentTimeMillis()));

        List<String> timeRanges = Arrays.asList("minute", "hour", "24hours");
        timeRanges.forEach(this::getEventsForTimePeriod);
    }

    private void getEventsForTimePeriod(String timeRange) {
        ResponseEntity<EventsResponse> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/events/period?range=" + timeRange, EventsResponse.class);
        assertThat(forEntity);
        assertThat(forEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(Objects.requireNonNull(forEntity.getBody()).getEvents()).isNotEmpty();
        assertThat(Objects.requireNonNull(forEntity.getBody()).getEvents().get(0).getName()).isEqualTo("name");
        assertThat(Objects.requireNonNull(forEntity.getBody()).getEvents().get(0).getDescription()).isEqualTo("description");
        assertThat(Objects.requireNonNull(forEntity.getBody()).getEvents().get(0).getTimestampMillisUTC()).isNotNull();
    }
}