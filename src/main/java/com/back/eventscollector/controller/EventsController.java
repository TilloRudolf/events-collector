package com.back.eventscollector.controller;

import com.back.eventscollector.model.Event;
import com.back.eventscollector.model.EventsResponse;
import com.back.eventscollector.repository.EventsRepository;
import com.back.eventscollector.repository.TimesEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/events")
@Tag(name = "events", description = "takes and gets events")
public class EventsController {

    private final EventsRepository repository;

    @Autowired
    public EventsController(EventsRepository repository) {
        this.repository = repository;
    }

    @Operation(summary = "Return events", description = "return by one of [minute, hour, 24hours]", tags = {"event"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = Event.class))
            )),
            @ApiResponse(responseCode = "405", description = "typo in parameter \"range\", correct values [minute, hour, 24hours]. For example: ?range=hour"),
            @ApiResponse(responseCode = "400", description = "parameter \"range\" is missing, correct values [minute, hour, 24hours]. For example: ?range=hour")
    })
    @RequestMapping(value = "/period", method = RequestMethod.GET)
    public ResponseEntity<EventsResponse> forPeriod(@RequestParam String range) {
        if (isValidRange(range)) {
            return new ResponseEntity<>(repository.forRange(range), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new EventsResponse(Collections.emptyList()), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private Boolean isValidRange(String range) {
        TimesEnum timeRange = TimesEnum.getByValue(range);
        return timeRange != null;
    }


    @Operation(summary = "Add new event", description = "You can add new Event with only UTC(+0) TimeStamp in millis.", tags = {"event"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "not valid value or values")
    })
    @RequestMapping(value = "/event", method = RequestMethod.PUT)
    public ResponseEntity<String> event(@Valid @RequestBody Event event) {
        repository.putEvent(event);
        return new ResponseEntity<>(
                String.format("%s%s%s", "event name: ", event.getName(), " saved"),
                HttpStatus.OK);
    }
}