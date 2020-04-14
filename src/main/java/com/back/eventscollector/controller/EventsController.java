package com.back.eventscollector.controller;

import com.back.eventscollector.model.Event;
import com.back.eventscollector.repository.EventsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventsController {

    private final EventsRepository repository = new EventsRepository();

    @GetMapping(value = "/minute")
    @ResponseBody
    public String eventsForLastMinute() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Event> events = repository.forMinute();
        return mapper.writeValueAsString(events);
    }

    @GetMapping(value = "/hour")
    @ResponseBody
    public String eventsForLastHour() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Event> events = repository.forHour();
        return mapper.writeValueAsString(events);
    }

    @GetMapping(value = "/day")
    @ResponseBody
    public String eventsForLastDay() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Event> events = repository.forDay();
        return mapper.writeValueAsString(events);
    }

    @PostMapping(value = "/event")
    @ResponseBody
    public String event(@RequestBody Event event) {
        repository.putEvent(event);
        return "OK";
    }
}