package com.back.eventscollector.repository;

public enum TimeUnitEnum {
    MINUTE_MILLIS(60 * 1000L),
    HOUR_MILLIS(60 * 60 * 1000L),
    DAY_MILLIS(24 * 60 * 60 * 1000L);

    private final long value;

    TimeUnitEnum(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}