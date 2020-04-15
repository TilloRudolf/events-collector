package com.back.eventscollector.controller;

import com.back.eventscollector.model.Event;
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
    public List<Event> eventsForLastMinute() {
        return repository.forMinute();
    }

    @RequestMapping(value = "/hour", method = RequestMethod.GET)
    @ResponseBody
    public List<Event> eventsForLastHour() {
        return repository.forHour();
    }

    @RequestMapping(value = "/day", method = RequestMethod.GET)
    @ResponseBody
    public List<Event> eventsForLastDay() {
        return repository.forDay();
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    @ResponseBody
    public String event(@RequestBody Event event) {
        repository.putEvent(event);
        return "OK";
    }
}