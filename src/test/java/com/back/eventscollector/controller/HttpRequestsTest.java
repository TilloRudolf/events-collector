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
        int codeValue = forEntity.getStatusCodeValue();
        assertThat(codeValue).isEqualTo(200);
        String collectionName = forEntity.getBody().getResult();
        assertThat(collectionName).isEqualTo(MINUTE_COLLECTION);
    }

    @Test
    public void handleEventFail() {
        ResponseEntity<ExceptionSerialize> forEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/events",
                new Event("name", "description", System.currentTimeMillis() + (60 * 1000)),
                ExceptionSerialize.class);
        int statusCode = forEntity.getStatusCodeValue();
        assertThat(statusCode).isEqualTo(400);
        int responseCode = forEntity.getBody().getCode();
        assertThat(responseCode).isEqualTo(400);
        String responseMessage = forEntity.getBody().getMessage();
        assertThat(responseMessage).contains("You should wait some time");
    }

    @Test
    public void handleTooOldEvent() {
        ResponseEntity<HandleEventResponse> forEntity = restTemplate.postForEntity(
                "http://localhost:" + port + "/events",
                new Event("name", "description", System.currentTimeMillis() - (24 * 60 * 60 * 1000)),
                HandleEventResponse.class);
        int statusCode = forEntity.getStatusCodeValue();
        assertThat(statusCode).isEqualTo(200);
        String resultMessage = forEntity.getBody().getResult();
        assertThat(resultMessage).isEqualTo(TOO_OLD);
    }

    @Test
    public void getMinuteShouldReturnOK() {
        ResponseEntity<EventsCount> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/events/minute", EventsCount.class);
        int statusCode = forEntity.getStatusCodeValue();
        assertThat(statusCode).isEqualTo(200);
    }

    @Test
    public void getHourShouldReturnOK() {
        ResponseEntity<EventsCount> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/events/hour", EventsCount.class);
        int statusCode = forEntity.getStatusCodeValue();
        assertThat(statusCode).isEqualTo(200);
    }

    @Test
    public void get24HoursShouldReturnOK() {
        ResponseEntity<EventsCount> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/events/24hours", EventsCount.class);
        int statusCode = forEntity.getStatusCodeValue();
        assertThat(statusCode).isEqualTo(200);
    }
}