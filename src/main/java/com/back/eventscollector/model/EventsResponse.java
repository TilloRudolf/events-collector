package com.back.eventscollector.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

public class EventsResponse {
    @JsonSerialize
    private final List<Event> events;

    public EventsResponse(List<Event> events) {
        this.events = events;
    }
}
