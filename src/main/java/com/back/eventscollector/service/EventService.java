package com.back.eventscollector.service;

import com.back.eventscollector.configs.CollectionName;
import com.back.eventscollector.model.Event;
import com.back.eventscollector.model.EventsCount;
import com.back.eventscollector.model.HandleEventResponse;

public interface EventService {
    HandleEventResponse handleEvent(Event event);

    EventsCount getCount(CollectionName timeCollection);
}
