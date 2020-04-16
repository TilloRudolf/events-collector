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
        repository = new EventsRepository(new TimeRepository());

        Long currentMillis = System.currentTimeMillis();

        Event event1 = new Event("hello", "for last minute", minusSeconds(currentMillis, 30));
        Event event2 = new Event("hello", "for last hour", minusMinutes(currentMillis, 10));
        Event event3 = new Event("hello", "for last 24 hour", minusHours(currentMillis, 10));
        Event event4 = new Event("hello", "very old", minusHours(currentMillis, 25));

        repository.putEvent(event1);
        repository.putEvent(event2);
        repository.putEvent(event3);
        repository.putEvent(event4);

        repository.recalculate();
    }

    @Test
    public void eventsForLastMinute() {
        List<Event> forLastMinute = repository.forRange("minute").getEvents();
        assertEquals(forLastMinute.size(), 1);
        assertEquals(forLastMinute.get(0).getDescription(), "for last minute");
    }

    @Test
    public void eventsForLastHour() {
        List<Event> forLastHour = repository.forRange("hour").getEvents();
        assertEquals(forLastHour.size(), 2);
        assertEquals(forLastHour.get(0).getDescription(), "for last minute");
    }

    @Test
    public void eventsForLast24Hour() {
        List<Event> forLast24Hour = repository.forRange("24hour").getEvents();
        assertEquals(forLast24Hour.size(), 3);
        assertEquals(forLast24Hour.get(0).getDescription(), "for last minute");
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