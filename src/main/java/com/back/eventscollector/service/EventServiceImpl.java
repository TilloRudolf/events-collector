package com.back.eventscollector.service;

import com.back.eventscollector.configs.CollectionName;
import com.back.eventscollector.configs.HourEntryListener;
import com.back.eventscollector.configs.MinuteEntryListener;
import com.back.eventscollector.exception.TimeBreakingException;
import com.back.eventscollector.model.Event;
import com.back.eventscollector.model.EventsCount;
import com.back.eventscollector.model.HandleEventResponse;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.back.eventscollector.configs.CollectionName.*;
import static com.back.eventscollector.configs.TimeRange.*;


@Service
public class EventServiceImpl implements EventService {

    private final HazelcastInstance hazelcastInstance;
    private final MinuteEntryListener minuteEntryListener;
    private final HourEntryListener hourEntryListener;

    private IMap<Long, Event> minuteEventCollection;
    private IMap<Long, Event> hourEventCollection;
    private IMap<Long, Event> dayEventCollection;

    private final AtomicLong eventId = new AtomicLong(0);

    public EventServiceImpl(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance,
                            MinuteEntryListener minuteEntryListener,
                            HourEntryListener hourEntryListener) {
        this.hazelcastInstance = hazelcastInstance;
        this.minuteEntryListener = minuteEntryListener;
        this.hourEntryListener = hourEntryListener;
    }

    @PostConstruct
    public void init() {
        minuteEventCollection = hazelcastInstance.getMap(MINUTE_COLLECTION.getName());
        hourEventCollection = hazelcastInstance.getMap(HOUR_COLLECTION.getName());
        dayEventCollection = hazelcastInstance.getMap(DAY_COLLECTION.getName());
        minuteEventCollection.addLocalEntryListener(minuteEntryListener);
        hourEventCollection.addLocalEntryListener(hourEntryListener);
    }

    @Override
    public HandleEventResponse handleEvent(Event event) {
        return new HandleEventResponse(chooseCollection(event));
    }

    @Override
    public EventsCount getCount(CollectionName timeCollection) {
        long count = 0;
        switch (timeCollection) {
            case MINUTE_COLLECTION:
                count = hazelcastInstance.getMap(MINUTE_COLLECTION.getName()).size();
                break;
            case HOUR_COLLECTION:
                count = hazelcastInstance.getMap(MINUTE_COLLECTION.getName()).size() +
                        hazelcastInstance.getMap(HOUR_COLLECTION.getName()).size();
                break;
            case DAY_COLLECTION:
                count = hazelcastInstance.getMap(MINUTE_COLLECTION.getName()).size() +
                        hazelcastInstance.getMap(HOUR_COLLECTION.getName()).size() +
                        hazelcastInstance.getMap(DAY_COLLECTION.getName()).size();
                break;
        }
        return new EventsCount(count);

    }

    private String chooseCollection(Event event) {
        long deltaTime = System.currentTimeMillis() - event.getTimestampMillisUTC();
        if (deltaTime < 0) {
            throw new TimeBreakingException(event);
        }

        long ttlForMinute = MILLIS_IN_MINUTE.getRange() - deltaTime;
        long ttlForHour = MILLIS_IN_HOUR.getRange() - deltaTime;
        long ttlForDay = MILLIS_IN_DAY.getRange() - deltaTime;
        if (deltaTime < MILLIS_IN_MINUTE.getRange()) {
            minuteEventCollection.set(eventId.getAndIncrement(), event, ttlForMinute, TimeUnit.MILLISECONDS);
            return MINUTE_COLLECTION.getName();
        } else if (deltaTime < MILLIS_IN_HOUR.getRange()) {
            hourEventCollection.set(eventId.getAndIncrement(), event, ttlForHour, TimeUnit.MILLISECONDS);
            return HOUR_COLLECTION.getName();
        } else if (deltaTime < MILLIS_IN_DAY.getRange()) {
            dayEventCollection.set(eventId.getAndIncrement(), event, ttlForDay, TimeUnit.MILLISECONDS);
            return DAY_COLLECTION.getName();
        }
        return TOO_OLD.getName();
    }
}