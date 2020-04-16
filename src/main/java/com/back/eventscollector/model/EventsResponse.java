package com.back.eventscollector.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EventsResponse {
    private final List<Event> events;

    @JsonCreator
    public EventsResponse(
            @JsonProperty("events") List<Event> events
    ) {
        this.events = events;
    }

    @JsonProperty("events")
    public List<Event> getEvents() {
        return events;
    }
}
