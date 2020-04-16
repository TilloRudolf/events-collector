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

        Event event1 = new Event("hello", "for minute", minusSeconds(currentMillis, 30));
        Event event2 = new Event("hello", "for hour", minusMinutes(currentMillis, 10));
        Event event3 = new Event("hello", "for day", minusHours(currentMillis, 10));
        Event event4 = new Event("hello", "very old", minusHours(currentMillis, 25));

        repository.putEvent(event1);
        repository.putEvent(event2);
        repository.putEvent(event3);
        repository.putEvent(event4);

        repository.recalculate();
    }

    @Test
    public void eventsForMinute() {
        List<Event> forMinute = repository.forRange("minute").getEvents();
        assertEquals(forMinute.size(), 1);
        assertEquals(forMinute.get(0).getDescription(), "for minute");
    }

    @Test
    public void eventsForHour() {
        List<Event> forHour = repository.forRange("hour").getEvents();
        assertEquals(forHour.size(), 2);
        assertEquals(forHour.get(0).getDescription(), "for minute");
    }

    @Test
    public void eventsForDay() {
        List<Event> forDay = repository.forRange("day").getEvents();
        assertEquals(forDay.size(), 3);
        assertEquals(forDay.get(0).getDescription(), "for minute");
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