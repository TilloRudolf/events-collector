package com.back.eventscollector.model;

public class EventItem {
    final Event event;
    final Long eventId;

    public EventItem(Event event, Long eventId) {
        this.event = event;
        this.eventId = eventId;
    }

    public Event getEvent() {
        return event;
    }

    public Long getEventId() {
        return eventId;
    }
}