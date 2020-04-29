package com.back.eventscollector.configs;

import com.back.eventscollector.model.Event;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.listener.EntryEvictedListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

import static com.back.eventscollector.configs.HazelcastProperties.HOUR_COLLECTION;
import static com.back.eventscollector.configs.HazelcastProperties.MILLIS_IN_HOUR;

@Service
public class MinuteEntryListener implements EntryEvictedListener<Long, Event> {

    private final HazelcastInstance hazelcastInstance;
    private IMap<Long, Event> hourEventsCollection;

    public MinuteEntryListener(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostConstruct
    public void init() {
        hourEventsCollection = hazelcastInstance.getMap(HOUR_COLLECTION);
    }

    @Override
    public void entryEvicted(EntryEvent<Long, Event> event) {
        long deltaTime = System.currentTimeMillis() - event.getOldValue().getTimestampMillisUTC();
        long ttlForHour = MILLIS_IN_HOUR - deltaTime;
        hourEventsCollection.set(event.getKey(), event.getOldValue(), ttlForHour, TimeUnit.MILLISECONDS);
    }
}
