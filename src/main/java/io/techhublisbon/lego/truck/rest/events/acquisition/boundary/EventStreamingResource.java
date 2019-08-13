package io.techhublisbon.lego.truck.rest.events.acquisition.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.techhublisbon.lego.truck.rest.errors.InputValidator;
import io.techhublisbon.lego.truck.rest.events.acquisition.control.EventStreamingHandler;
import io.techhublisbon.lego.truck.rest.settings.control.WebSocketConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "Operations pertaining the real time streaming of events")
public class EventStreamingResource {

    private EventStreamingHandler eventStreamingHandler;
    private WebSocketConfig config;
    private InputValidator inputValidator;

    @Autowired
    public EventStreamingResource(EventStreamingHandler eventStreamingHandler, WebSocketConfig config, InputValidator inputValidator) {
        this.eventStreamingHandler = eventStreamingHandler;
        this.config = config;
        this.inputValidator = inputValidator;
    }

    @ApiOperation(value = "Returns the topic of the websocket with the request event stream")
    @GetMapping("/events/stream/{interval}/{fields}/{transformation}")
    public ResponseEntity<String> getStreamPath(@PathVariable int interval, @PathVariable String fields, @PathVariable String transformation) {

        inputValidator.checkValidStreamingComponents(fields, transformation);
        inputValidator.checkValidTransformation(transformation);
        inputValidator.checkThatIntervalIsBiggerThanStorageInterval(fields, interval);
        String streamString = eventStreamingHandler.getStream(interval, fields, transformation);
        return ResponseEntity.ok().body(streamString);
    }

    @ApiOperation(value = "Returns the endpoint of the websocket handler, to start the connection")
    @GetMapping("/events/stream")
    public ResponseEntity<String> getWebSocketEndpoint() {
        String endpoint = config.getEndpoint();
        return ResponseEntity.ok().body(endpoint);
    }


}
