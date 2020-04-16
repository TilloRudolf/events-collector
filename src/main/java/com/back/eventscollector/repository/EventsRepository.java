package com.back.eventscollector.repository;

import com.back.eventscollector.model.Event;
import com.back.eventscollector.model.EventItem;
import com.back.eventscollector.model.EventsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component("eventRepository")
public class EventsRepository {
    private final TimeRepository repository;
    private final AtomicLong nextEventId = new AtomicLong(0);

    @Autowired
    public EventsRepository(TimeRepository repository){
        this.repository = repository;
    }

    public void putEvent(Event event) {
        EventItem item = new EventItem(event, nextEventId.incrementAndGet());
        TimeRepositoryNode minutes = repository.getNode(TimesEnum.getFirst());
        minutes.putEvent(item.getEventId(), item);
    }

    public EventsResponse forRange(String range) {
        TimeRepositoryNode node = repository.getNode(TimesEnum.getByValue(range));
        List<Event> events = node.collectEvents();
        return new EventsResponse(events);
    }

    public void recalculate() {
        repository.recalculateNodes();
    }
}