package com.back.eventscollector.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "hazelcast")
public class HazelcastProperties {

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
