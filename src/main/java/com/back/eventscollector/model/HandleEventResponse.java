package com.back.eventscollector.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HandleEventResponse {
    private final String result;

    @JsonCreator
    public HandleEventResponse(@JsonProperty("result") String result) {
        this.result = result;
    }

    @JsonProperty("result")
    public String getResult() {
        return result;
    }
}
