package com.back.eventscollector.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("repositoryCleanScheduler")
public class ScheduledRepositoryCleaner {

    private final EventsRepository repository;

    @Autowired
    public ScheduledRepositoryCleaner(EventsRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 10 * 1000L)
    public void cleanOldEvents() {
        repository.recalculate();
    }
}
