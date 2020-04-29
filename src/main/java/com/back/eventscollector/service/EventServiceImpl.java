package com.back.eventscollector.service;

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

import static com.back.eventscollector.configs.HazelcastProperties.*;


@Service
public class EventServiceImpl implements EventService {

    private final HazelcastInstance hazelcastInstance;
    private final MinuteEntryListener minuteEntryListener;
    private final HourEntryListener hourEntryListener;

    private IMap<Long, Event> minuteEventCollection;
    private IMap<Long, Event> hourEventCollection;
    private IMap<Long, Event> _24hoursEventCollection;

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
        minuteEventCollection = hazelcastInstance.getMap(MINUTE_COLLECTION);
        hourEventCollection = hazelcastInstance.getMap(HOUR_COLLECTION);
        _24hoursEventCollection = hazelcastInstance.getMap(_24_HOURS_COLLECTION);
        minuteEventCollection.addLocalEntryListener(minuteEntryListener);
        hourEventCollection.addLocalEntryListener(hourEntryListener);
    }

    @Override
    public HandleEventResponse handleEvent(Event event) {
        return new HandleEventResponse(chooseCollection(event));
    }

    @Override
    public EventsCount getCount(String timeCollection) {
        long count = 0;
        switch (timeCollection) {
            case MINUTE_COLLECTION:
                count = hazelcastInstance.getMap(MINUTE_COLLECTION).size();
                break;
            case HOUR_COLLECTION:
                count = hazelcastInstance.getMap(MINUTE_COLLECTION).size() +
                        hazelcastInstance.getMap(HOUR_COLLECTION).size();
                break;
            case _24_HOURS_COLLECTION:
                count = hazelcastInstance.getMap(MINUTE_COLLECTION).size() +
                        hazelcastInstance.getMap(HOUR_COLLECTION).size() +
                        hazelcastInstance.getMap(_24_HOURS_COLLECTION).size();
                break;
        }
        return new EventsCount(count);

    }

    private String chooseCollection(Event event) {
        long deltaTime = System.currentTimeMillis() - event.getTimestampMillisUTC();
        if (deltaTime < 0) {
            throw new TimeBreakingException(event);
        }

        long ttlForMinute = MILLIS_IN_MINUTE - deltaTime;
        long ttlForHour = MILLIS_IN_HOUR - deltaTime;
        long ttlFor24Hours = MILLIS_IN_24_HOURS - deltaTime;
        if (deltaTime < MILLIS_IN_MINUTE) {
            minuteEventCollection.set(eventId.getAndIncrement(), event, ttlForMinute, TimeUnit.MILLISECONDS);
            return MINUTE_COLLECTION;
        } else if (deltaTime < MILLIS_IN_HOUR) {
            hourEventCollection.set(eventId.getAndIncrement(), event, ttlForHour, TimeUnit.MILLISECONDS);
            return HOUR_COLLECTION;
        } else if (deltaTime < MILLIS_IN_24_HOURS) {
            _24hoursEventCollection.set(eventId.getAndIncrement(), event, ttlFor24Hours, TimeUnit.MILLISECONDS);
            return _24_HOURS_COLLECTION;
        }
        return TOO_OLD;
    }
}