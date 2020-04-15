package com.back.eventscollector.controller;

import com.back.eventscollector.model.Event;
import com.back.eventscollector.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventsController {

    @Autowired
    private EventsRepository repository;

    @GetMapping(value = "/minute")
    @ResponseBody
    public List<Event> eventsForLastMinute() {
        return repository.forMinute();
    }

    @GetMapping(value = "/hour")
    @ResponseBody
    public List<Event> eventsForLastHour() {
        return repository.forHour();
    }

    @GetMapping(value = "/day")
    @ResponseBody
    public List<Event> eventsForLastDay() {
        return repository.forDay();
    }

    @PostMapping(value = "/event")
    @ResponseBody
    public String event(@RequestBody Event event) {
        repository.putEvent(event);
        return "OK";
    }
}