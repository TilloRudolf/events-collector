package com.back.eventscollector.repository;

import com.back.eventscollector.model.Event;
import com.back.eventscollector.model.EventItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component("eventRepository")
public class EventsRepository {
    Map<Long, EventItem> map = new LinkedHashMap<>();
    AtomicLong nextEventId = new AtomicLong(0);

    private final static Long MINUTE_MILLIS = 60 * 1000L;
    private final static Long HOUR_MILLIS = 60 * 60 * 1000L;
    private final static Long DAY_MILLIS = 24 * 60 * 60 * 1000L;

    public void putEvent(Event event) {
        EventItem item = new EventItem(event, nextEventId.incrementAndGet());
        map.put(item.getEventId(), item);
    }

    public List<Event> forMinute() {
        Long minuteBefore = System.currentTimeMillis() - MINUTE_MILLIS;
        return events(minuteBefore);
    }

    public List<Event> forHour() {
        Long minuteBefore = System.currentTimeMillis() - HOUR_MILLIS;
        return events(minuteBefore);
    }

    public List<Event> forDay() {
        Long minuteBefore = System.currentTimeMillis() - DAY_MILLIS;
        return events(minuteBefore);
    }

    private List<Event> events(Long timestampBefore) {
        List<Event> list = new ArrayList<>();
        List<Long> idsForRemove = new ArrayList<>();

        map.forEach((id, item) -> {
            Long dayBefore = System.currentTimeMillis() - DAY_MILLIS;
            Long eventTimestamp = item.getEvent().getTimestampMillis();

            if (eventTimestamp < dayBefore) {
                idsForRemove.add(id);
            } else {
                if (eventTimestamp > timestampBefore) {
                    list.add(item.getEvent());
                }
            }
        });

        idsForRemove.forEach(id -> map.remove(id));
        return list;
    }
}