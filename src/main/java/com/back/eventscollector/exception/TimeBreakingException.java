package com.back.eventscollector.exception;

import com.back.eventscollector.model.Event;

import java.util.Date;

public class TimeBreakingException extends RuntimeException {
    public TimeBreakingException(Event event) {
        super(String.format("time of event: %s is in the future. You should wait some time",
                new Date(event.getTimestampMillisUTC())));
    }
}
