package io.techhublisbon.lego.truck.rest.events.acquisition.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.techhublisbon.lego.truck.rest.errors.InputValidator;
import io.techhublisbon.lego.truck.rest.events.acquisition.control.EventDataHandler;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Interval", description = "Operations pertaining to events in the last X time units")
public class EventIntervalResource {

    private EventDataHandler eventDataHandler;
    private InputValidator inputValidator;

    @Autowired
    public EventIntervalResource(EventDataHandler eventDataHandler, InputValidator inputValidator) {
        this.eventDataHandler = eventDataHandler;
        this.inputValidator = inputValidator;
    }

    @ApiOperation(value = "Returns the last value acquired from the requested components", response = Event.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = Event.class)})
    @GetMapping(value = "/events/last/{component}", produces = "application/json")
    public ResponseEntity<Event> getComponentLastEvent(@PathVariable String component) {

        inputValidator.checkValidComponent(component);

        final Event lastEvent = eventDataHandler.getTransformedEvent(1000, component, "last");
        return ResponseEntity.ok().body(lastEvent);
    }


    @ApiOperation(value = "Applies the transform to the events that happened in the last X seconds" + " and return.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Ok", response = Event.class)})
    @GetMapping(value = "/events/{component}/{interval}/{transformation}", produces = "application/json")
    public ResponseEntity<Event> getComponentEventInSamplingInterval(@PathVariable String component, @PathVariable long interval, @PathVariable String transformation) {
        inputValidator.checkValidComponent(component);
        inputValidator.checkValidTransformation(transformation);
        inputValidator.checkThatIntervalIsBiggerThanStorageInterval(component, interval);

        final Event lastEvent = eventDataHandler.getTransformedEvent(interval, component, transformation);
        return ResponseEntity.ok().body(lastEvent);
    }


}
