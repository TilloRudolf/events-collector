package com.back.eventscollector.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExceptionSerialize {

    private final String message;

    private final int code;

    @JsonCreator
    public ExceptionSerialize(@JsonProperty("message") String message, @JsonProperty("code") int code) {
        this.message = message;
        this.code = code;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("code")
    public int getCode() {
        return code;
    }
}
