package com.back.eventscollector.controller;

import com.back.eventscollector.model.Event;
import com.back.eventscollector.model.EventsResponse;
import com.back.eventscollector.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/events")
public class EventsController {

    private final EventsRepository repository;

    @Autowired
    public EventsController(EventsRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/period", method = RequestMethod.GET)
    public EventsResponse forPeriod(@RequestParam String range) {
        return repository.forRange(range);
    }

    @RequestMapping(value = "/event", method = RequestMethod.POST)
    public ResponseEntity<String> event(@Valid @RequestBody Event event) {
        repository.putEvent(event);
        return new ResponseEntity<>(
                String.format("%s%s%s", "event name: ", event.getName(), " saved"),
                HttpStatus.OK);
    }
}