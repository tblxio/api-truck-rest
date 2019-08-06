package dtb.lego.truck.rest.data.events.acquisition.boundary;

import dtb.lego.truck.rest.data.events.acquisition.control.DataStreamingHandler;
import dtb.lego.truck.rest.errors.InputValidator;
import dtb.lego.truck.rest.settings.control.WebSocketConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "Operations pertaining the real time streaming of events")
public class EventStreamingResource {

    @Autowired
    DataStreamingHandler dataStreamingHandler;

    @Autowired
    WebSocketConfig config;

    @Autowired
    InputValidator inputValidator;


    @ApiOperation(value = "Returns the topic of the websocket with the request event stream")
    @GetMapping("/events/stream/{interval}/{fields}/{transformation}")
    public ResponseEntity<String> getStreamPath(@PathVariable int interval,
                                                @PathVariable String fields,
                                                @PathVariable String transformation) {

        inputValidator.checkValidStreamingComponents(fields, transformation);
        inputValidator.checkValidTransformation(transformation);
        inputValidator.checkThatIntervalIsBiggerThanStorageInterval(interval);
        String streamString = dataStreamingHandler.getStream(interval, fields, transformation);
        return ResponseEntity.ok()
                .body(streamString);
    }

    @ApiOperation(value = "Returns the endpoint of the websocket handler, to start the connection")
    @GetMapping("/events/stream")
    public ResponseEntity<String> getWebSocketEndpoint() {
        String endpoint = config.getEndpoint();
        return ResponseEntity.ok()
                .body(endpoint);
    }


}
