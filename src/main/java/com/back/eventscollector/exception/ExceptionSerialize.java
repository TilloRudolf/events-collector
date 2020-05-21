package com.back.eventscollector.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ExceptionWrapper", description = "wrapping exception for server response.")
public class ExceptionSerialize {

    @Schema(required = true)
    private final String message;

    @Schema(required = true)
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
