package com.back.eventscollector.configs;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.eviction.LRUEvictionPolicy;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.back.eventscollector.configs.CollectionName.*;
import static com.hazelcast.config.MaxSizeConfig.MaxSizePolicy.USED_HEAP_PERCENTAGE;

@Configuration
@EnableCaching
@EnableConfigurationProperties(value = HazelcastProperties.class)
public class CacheConfig {

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance hazelcastInstance(final HazelcastProperties hazelcastProperties) {
        final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(new Config());
        final MapConfig minuteEventsMapConfig = new MapConfig(MINUTE_COLLECTION.getName())
                .setReadBackupData(hazelcastProperties.isReadBackupData())
                .setBackupCount(hazelcastProperties.getBackupCount())
                .setMapEvictionPolicy(new LRUEvictionPolicy())
                .setMaxSizeConfig(new MaxSizeConfig()
                        .setSize(hazelcastProperties.getMaxCacheSize())
                        .setMaxSizePolicy(USED_HEAP_PERCENTAGE));
        final MapConfig hourEventsMapConfig = new MapConfig(HOUR_COLLECTION.getName())
                .setReadBackupData(hazelcastProperties.isReadBackupData())
                .setBackupCount(hazelcastProperties.getBackupCount())
                .setMapEvictionPolicy(new LRUEvictionPolicy())
                .setMaxSizeConfig(new MaxSizeConfig()
                        .setSize(hazelcastProperties.getMaxCacheSize())
                        .setMaxSizePolicy(USED_HEAP_PERCENTAGE));
        final MapConfig dayEventsMapConfig = new MapConfig(DAY_COLLECTION.getName())
                .setReadBackupData(hazelcastProperties.isReadBackupData())
                .setBackupCount(hazelcastProperties.getBackupCount())
                .setMapEvictionPolicy(new LRUEvictionPolicy())
                .setMaxSizeConfig(new MaxSizeConfig()
                        .setSize(hazelcastProperties.getMaxCacheSize())
                        .setMaxSizePolicy(USED_HEAP_PERCENTAGE));
        hazelcastInstance.getConfig()
                .addMapConfig(minuteEventsMapConfig)
                .addMapConfig(hourEventsMapConfig)
                .addMapConfig(dayEventsMapConfig);
        return hazelcastInstance;
    }

    @Bean
    public CacheManager cacheManager(@Qualifier("hazelcastInstance") final HazelcastInstance hazelcastInstance) {
        return new HazelcastCacheManager(hazelcastInstance);
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return null;
    }
}
