package com.back.eventscollector.repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public enum TimesEnum {
    MINUTE("minute", TimeUnitEnum.MINUTE_MILLIS),
    HOUR("hour", TimeUnitEnum.HOUR_MILLIS),
    DAY("day", TimeUnitEnum.DAY_MILLIS);

    private final String value;
    private final TimeUnitEnum timeRange;

    TimesEnum(String value, TimeUnitEnum timeRange) {
        this.value = value;
        this.timeRange = timeRange;
    }

    public String getValue() {
        return value;
    }

    public TimeUnitEnum getTimeRange() {
        return timeRange;
    }

    public static Stream<TimesEnum> stream() {
        return Stream.of(TimesEnum.values());
    }

    public static TimesEnum getFirst() {
        return TimesEnum.values()[0];
    }

    public static TimesEnum getByValue(String value) {
        List<TimesEnum> list = TimesEnum.stream().collect(Collectors.toList());
        TimesEnum timeEnum = null;
        for (TimesEnum e : list) {
            if (e.value.equals(value)) timeEnum = e;
        }
        return timeEnum;
    }
}
