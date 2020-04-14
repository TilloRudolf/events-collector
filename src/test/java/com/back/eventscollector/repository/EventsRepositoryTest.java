package com.back.eventscollector.repository;

import com.back.eventscollector.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EventsRepositoryTest {
    EventsRepository repository;

    @BeforeEach
    public void initRepository() {
        repository = new EventsRepository();

        Long currentMillis = System.currentTimeMillis();

        Event event1 = new Event("hello", "1", minusSeconds(currentMillis, 30));
        Event event2 = new Event("hello", "2", minusMinutes(currentMillis, 10));
        Event event3 = new Event("hello", "3", minusHours(currentMillis, 10));
        Event event4 = new Event("hello", "4", minusHours(currentMillis, 25));

        repository.putEvent(event1);
        repository.putEvent(event2);
        repository.putEvent(event3);
        repository.putEvent(event4);
    }

    @Test
    public void eventsForMinute() {
       List<Event> forMinute =  repository.forMinute();
       assertEquals(forMinute.size(), 1);
    }

    @Test
    public void eventsForHour() {
       List<Event> forMinute =  repository.forHour();
       assertEquals(forMinute.size(), 2);
    }

    @Test
    public void eventsForDay() {
       List<Event> forMinute =  repository.forDay();
       assertEquals(forMinute.size(), 3);
    }

    private Long minusSeconds(Long currentMillis, int seconds) {
        return currentMillis - (seconds * 1000);
    }

    private Long minusMinutes(Long currentMillis, int minutes) {
        return currentMillis - (minutes * 60 * 1000);
    }

    private Long minusHours(Long currentMillis, int hours) {
        return currentMillis - (hours * 60 * 60 * 1000);
    }
}