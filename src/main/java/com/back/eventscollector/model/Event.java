package com.back.eventscollector.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Event {
    @JsonSerialize
    final String name;
    @JsonSerialize
    final String description;
    @JsonSerialize
    final Long timestampMillis;

    public Event(String name, String description, Long timestampMillis) {
        this.name = name;
        this.description = description;
        this.timestampMillis = timestampMillis;
    }

    public Long getTimestampMillis() {
        return timestampMillis;
    }
}