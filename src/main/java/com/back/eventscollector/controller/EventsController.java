package com.back.eventscollector.controller;

import com.back.eventscollector.exception.ExceptionSerialize;
import com.back.eventscollector.model.Event;
import com.back.eventscollector.model.EventsCount;
import com.back.eventscollector.model.HandleEventResponse;
import com.back.eventscollector.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.back.eventscollector.configs.HazelcastProperties.*;

@RestController
@RequestMapping("/events")
@Tag(name = "events", description = "takes events and gets event count")
public class EventsController {

    private final EventService service;

    @Autowired
    public EventsController(EventService service) {
        this.service = service;
    }

    @Operation(summary = "Handle event", description = "You can add new Event with only UTC(+0) TimeStamp in millis.", tags = {"event"})
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "event is successfully handled",
                    content = @Content(schema = @Schema(implementation = HandleEventResponse.class))),
            @ApiResponse(responseCode = "400", description = "event isn't handled",
                    content = @Content(schema = @Schema(implementation = ExceptionSerialize.class)))
    })
    public HandleEventResponse handleEvent(@RequestBody @Valid Event event) {
        return service.handleEvent(event);
    }

    @Operation(summary = "Events count for current minute")
    @GetMapping("/minute")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "events count for current minute",
                    content = @Content(schema = @Schema(implementation = EventsCount.class)))
    })
    public EventsCount getMinuteEventsCount() {
        return service.getCount(MINUTE_COLLECTION);
    }

    @Operation(summary = "Events count for current hour")
    @GetMapping("/hour")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "events count for current hour",
                    content = @Content(schema = @Schema(implementation = EventsCount.class)))
    })
    public EventsCount getHourEventsCount() {
        return service.getCount(HOUR_COLLECTION);
    }

    @Operation(summary = "Events count for current 24 hours")
    @GetMapping("/24hours")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "events count for current 24 hours",
                    content = @Content(schema = @Schema(implementation = EventsCount.class)))
    })
    public EventsCount getDayEventsCount() {
        return service.getCount(DAY_COLLECTION);
    }
}