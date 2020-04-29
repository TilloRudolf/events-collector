package com.back.eventscollector.controller;

import com.back.eventscollector.exception.ExceptionSerialize;
import com.back.eventscollector.model.Event;
import com.back.eventscollector.model.EventsCount;
import com.back.eventscollector.model.HandleEventResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static com.back.eventscollector.configs.HazelcastProperties.MINUTE_COLLECTION;
import static com.back.eventscollector.configs.HazelcastProperties.TOO_OLD;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestsTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void handleEventSuccessful() {
        ResponseEntity<HandleEventResponse> forEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/events",
                new Event("name", "description", System.currentTimeMillis()),
                HandleEventResponse.class);
        assertThat(forEntity);
        assertThat(forEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(Objects.requireNonNull(Objects.requireNonNull(forEntity.getBody()).getResult())).isEqualTo(MINUTE_COLLECTION);
    }

    @Test
    public void handleEventFail() {
        ResponseEntity<ExceptionSerialize> forEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/events",
                new Event("name", "description", System.currentTimeMillis() + (60 * 1000)),
                ExceptionSerialize.class);
        assertThat(forEntity);
        assertThat(forEntity.getStatusCodeValue()).isEqualTo(400);
        assertThat(Objects.requireNonNull(forEntity.getBody()).getCode()).isEqualTo(400);
        assertThat(Objects.requireNonNull(Objects.requireNonNull(forEntity.getBody()).getMessage())).contains("You should wait some time");
    }

    @Test
    public void handleTooOldEvent() {
        ResponseEntity<HandleEventResponse> forEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/events",
                new Event("name", "description", System.currentTimeMillis() - (24 * 60 * 60 * 1000)),
                HandleEventResponse.class);
        assertThat(forEntity);
        assertThat(forEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(Objects.requireNonNull(Objects.requireNonNull(forEntity.getBody()).getResult())).isEqualTo(TOO_OLD);
    }

    @Test
    public void getMinuteShouldReturnOK() {
        ResponseEntity<EventsCount> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/events/minute", EventsCount.class);
        assertThat(forEntity);
        assertThat(forEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void getHourShouldReturnOK() {
        ResponseEntity<EventsCount> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/events/hour", EventsCount.class);
        assertThat(forEntity);
        assertThat(forEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void get24HoursShouldReturnOK() {
        ResponseEntity<EventsCount> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/events/24hours", EventsCount.class);
        assertThat(forEntity);
        assertThat(forEntity.getStatusCodeValue()).isEqualTo(200);
    }
}