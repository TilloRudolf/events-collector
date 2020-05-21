package com.back.eventscollector.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventsCount {
    private final Long eventsCount;

    @JsonCreator
    public EventsCount(@JsonProperty("eventsCount") Long eventsCount) {
        this.eventsCount = eventsCount;
    }

    @JsonProperty("eventsCount")
    public Long getEventsCount() {
        return eventsCount;
    }
}
