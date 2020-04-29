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

import static com.back.eventscollector.configs.HazelcastProperties.MILLIS_IN_24_HOURS;
import static com.back.eventscollector.configs.HazelcastProperties._24_HOURS_COLLECTION;

@Service
public class HourEntryListener implements EntryEvictedListener<Long, Event> {

    private final HazelcastInstance hazelcastInstance;
    private IMap<Long, Event> _24hoursEventsCollection;

    public HourEntryListener(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostConstruct
    public void init() {
        _24hoursEventsCollection = hazelcastInstance.getMap(_24_HOURS_COLLECTION);
    }

    @Override
    public void entryEvicted(EntryEvent<Long, Event> event) {
        long deltaTime = System.currentTimeMillis() - event.getOldValue().getTimestampMillisUTC();
        long ttlFor24Hours = MILLIS_IN_24_HOURS - deltaTime;
        _24hoursEventsCollection.set(event.getKey(), event.getOldValue(), ttlFor24Hours, TimeUnit.MILLISECONDS);
    }
}
