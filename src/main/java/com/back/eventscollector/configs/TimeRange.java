package com.back.eventscollector.configs;

public enum TimeRange {
    MILLIS_IN_MINUTE(1000 * 60L),
    MILLIS_IN_HOUR(1000 * 60 * 60L),
    MILLIS_IN_DAY(1000 * 60 * 60 * 24L);

    private final Long range;

    TimeRange(Long range){
        this.range = range;
    }

    public Long getRange() {
        return range;
    }
}
