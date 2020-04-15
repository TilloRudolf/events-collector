package com.back.eventscollector.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class EventsControllerResponse {
    @JsonSerialize
    private final String msg;
    @JsonSerialize
    private final String eventName;

    public EventsControllerResponse(String msg, String eventName) {
        this.msg = msg;
        this.eventName = eventName;
    }
}
