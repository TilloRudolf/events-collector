package com.back.eventscollector.repository;

import com.back.eventscollector.model.Event;
import com.back.eventscollector.model.EventItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimeRepositoryNode {
    private final Map<Long, EventItem> events = new ConcurrentHashMap<>();
    private TimeRepositoryNode nextNode;

    private final TimeRepositoryNode previousNode;
    private final TimesEnum time;

    public TimeRepositoryNode(
            TimeRepositoryNode previousNode,
            TimesEnum time) {
        this.previousNode = previousNode;
        this.time = time;
    }

    public void setNextNode(TimeRepositoryNode node) {
        if (this.nextNode == null) {
            this.nextNode = node;
        }
    }

    public TimeRepositoryNode getNextNode() {
        return nextNode;
    }

    public List<Event> collectEvents() {
        List<Event> list = new ArrayList<>();
        if (previousNode != null) {
            list.addAll(previousNode.collectEvents());
        }
        events.forEach((k, v) -> list.add(v.getEvent()));
        return list;
    }

    public TimesEnum getTime() {
        return time;
    }

    public Map<Long, EventItem> getEvents() {
        return events;
    }

    public void putEvent(Long key, EventItem value) {
        events.put(key, value);
    }

    public void removeEvent(Long key) {
        events.remove(key);
    }

    public EventItem getEvent(Long key) {
        return events.get(key);
    }
}
