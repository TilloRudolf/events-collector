package com.back.eventscollector.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "hazelcast")
public class HazelcastProperties {

    public static final Long MILLIS_IN_MINUTE = 1000 * 60L;
    public static final Long MILLIS_IN_HOUR = 1000 * 60 * 60L;
    public static final Long MILLIS_IN_24_HOURS = 1000 * 60 * 60 * 24L;
    public static final String MINUTE_COLLECTION = "minute-collection";
    public static final String HOUR_COLLECTION = "hour-collection";
    public static final String _24_HOURS_COLLECTION = "24-hours-collection";
    public final static String TOO_OLD = "not handled, was more than day ago";

    private boolean readBackupData;
    private int backupCount;
    private int maxCacheSize;

    public void setReadBackupData(boolean readBackupData) {
        this.readBackupData = readBackupData;
    }

    public void setBackupCount(int backupCount) {
        this.backupCount = backupCount;
    }

    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
    }

    public boolean isReadBackupData() {
        return readBackupData;
    }

    public int getBackupCount() {
        return backupCount;
    }

    public int getMaxCacheSize() {
        return maxCacheSize;
    }
}
