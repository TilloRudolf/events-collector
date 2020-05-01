package com.back.eventscollector.service;

import com.back.eventscollector.model.Event;
import com.back.eventscollector.model.EventsCount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.back.eventscollector.configs.HazelcastProperties.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Test
    public void checkEvictionLogic() throws InterruptedException {
        Event minuteEvent = new Event("name", "created 57 seconds before", System.currentTimeMillis() - (MILLIS_IN_MINUTE - 5 * 1000));
        Event hourEvent = new Event("name", "created 59 minute 57 seconds before", System.currentTimeMillis() - (MILLIS_IN_HOUR - 5 * 1000));
        Event dayEvent = new Event("name", "created 23 hours 59 minute 57 seconds before", System.currentTimeMillis() - (MILLIS_IN_DAY - 5 * 1000));

        eventService.handleEvent(minuteEvent);
        eventService.handleEvent(hourEvent);
        eventService.handleEvent(dayEvent);

        EventsCount minuteCount = eventService.getCount(MINUTE_COLLECTION);
        EventsCount hourCount = eventService.getCount(HOUR_COLLECTION);
        EventsCount dayCount = eventService.getCount(DAY_COLLECTION);

        assertThat(minuteCount.getEventsCount()).isEqualTo(1);
        assertThat(hourCount.getEventsCount()).isEqualTo(2);
        assertThat(dayCount.getEventsCount()).isEqualTo(3);

        Thread.sleep(5 * 1000);

        EventsCount minuteCountAfterSleep = eventService.getCount(MINUTE_COLLECTION);
        EventsCount hourCountAfterSleep = eventService.getCount(HOUR_COLLECTION);
        EventsCount dayCountAfterSleep = eventService.getCount(DAY_COLLECTION);

        assertThat(minuteCountAfterSleep.getEventsCount()).isEqualTo(0);
        assertThat(hourCountAfterSleep.getEventsCount()).isEqualTo(1);
        assertThat(dayCountAfterSleep.getEventsCount()).isEqualTo(2);
    }
}
