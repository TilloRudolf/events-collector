package com.back.eventscollector.controller;

import com.back.eventscollector.model.Event;
import com.back.eventscollector.model.EventsResponse;
import com.back.eventscollector.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EventsController {

    @Autowired
    private final EventsRepository repository;

    public EventsController(EventsRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/minute", method = RequestMethod.GET)
    @ResponseBody
    public EventsResponse eventsForLastMinute() {
        List<Event> events = repository.forMinute();
        return new EventsResponse(events);
    }

    @RequestMapping(value = "/hour", method = RequestMethod.GET)
    @ResponseBody
    public EventsResponse eventsForLastHour() {
        List<Event> events = repository.forHour();
        return new EventsResponse(events);
    }

    @RequestMapping(value = "/day", method = RequestMethod.GET)
    @ResponseBody
    public EventsResponse eventsForLastDay() {
        List<Event> events = repository.forDay();
        return new EventsResponse(events);
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    @ResponseBody
    public EventsControllerResponse event(@RequestBody Event event) {
        repository.putEvent(event);
        return new EventsControllerResponse("SUCCESS", event.getName());
    }
}