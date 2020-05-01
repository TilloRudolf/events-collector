package com.back.eventscollector.exception;

import com.back.eventscollector.model.Event;

import java.time.Instant;

public class TimeBreakingException extends RuntimeException {
    public TimeBreakingException(Event event) {
        super(String.format("time of event: %s (UTC) is in the future. You should wait some time",
                Instant.ofEpochMilli(event.getTimestampMillisUTC())));
    }
}
