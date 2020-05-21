package com.back.eventscollector.configs;

public enum CollectionName {
    MINUTE_COLLECTION("minute-collection"),
    HOUR_COLLECTION("hour-collection"),
    DAY_COLLECTION("24-hours-collection"),
    TOO_OLD("not handled, was more than day ago");

    private final String name;

    CollectionName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
