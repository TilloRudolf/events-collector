package com.back.eventscollector.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Event {
    private final String name;
    private final String description;
    private final Long timestampMillis;

    @JsonCreator
    public Event(
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("timestampMillis") Long timestampMillis) {
        this.name = name;
        this.description = description;
        this.timestampMillis = timestampMillis;
    }

    public Long getTimestampMillis() {
        return timestampMillis;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}