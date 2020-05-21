package com.back.eventscollector.controller;

import com.back.eventscollector.exception.ExceptionSerialize;
import com.back.eventscollector.exception.TimeBreakingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class EventsControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(EventsControllerAdvice.class);

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, TimeBreakingException.class})
    @ResponseStatus(BAD_REQUEST)
    public ExceptionSerialize validationExceptionHandling(final Exception e) {
        ExceptionSerialize exception = new ExceptionSerialize(
                e.toString(),
                BAD_REQUEST.value()
        );
        log.error(exception.getMessage());
        return exception;
    }

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ExceptionSerialize internalErrorHandling(final Exception e) {
        ExceptionSerialize exception = new ExceptionSerialize(
                String.format("Internal server error: %s", e.toString()),
                INTERNAL_SERVER_ERROR.value()
        );
        log.error(exception.getMessage());
        return exception;
    }
}
