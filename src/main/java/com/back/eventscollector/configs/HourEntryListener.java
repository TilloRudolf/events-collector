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

import static com.back.eventscollector.configs.TimeRange.MILLIS_IN_DAY;

@Service
public class HourEntryListener implements EntryEvictedListener<Long, Event> {

    private final HazelcastInstance hazelcastInstance;
    private IMap<Long, Event> dayEventsCollection;

    public HourEntryListener(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostConstruct
    public void init() {
        dayEventsCollection = hazelcastInstance.getMap(CollectionName.DAY_COLLECTION.getName());
    }

    @Override
    public void entryEvicted(EntryEvent<Long, Event> event) {
        long deltaTime = System.currentTimeMillis() - event.getOldValue().getTimestampMillisUTC();
        long ttlForDay = MILLIS_IN_DAY.getRange() - deltaTime;
        dayEventsCollection.set(event.getKey(), event.getOldValue(), ttlForDay, TimeUnit.MILLISECONDS);
    }
}
