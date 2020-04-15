package com.back.eventscollector.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class EventsControllerResponse {
    private final String msg;
    private final String eventName;

    @JsonCreator
    public EventsControllerResponse(
            @JsonProperty("msg") String msg,
            @JsonProperty("eventName") String eventName
    ) {
        this.msg = msg;
        this.eventName = eventName;
    }

    public String getMsg() {
        return msg;
    }

    public String getEventName() {
        return eventName;
    }
}
