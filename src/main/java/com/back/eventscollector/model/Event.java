package com.back.eventscollector.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class Event implements Serializable {

    @NotNull
    @Size(min = 1, max = 50)
    private final String name;

    @NotNull
    @Size(max = 50)
    private final String description;

    @NotNull
    private final Long timestampMillisUTC;

    @JsonCreator
    public Event(
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("timestampMillisUTC") Long timestampMillisUTC) {
        this.name = name;
        this.description = description;
        this.timestampMillisUTC = timestampMillisUTC;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("timestampMillisUTC")
    public Long getTimestampMillisUTC() {
        return timestampMillisUTC;
    }
}