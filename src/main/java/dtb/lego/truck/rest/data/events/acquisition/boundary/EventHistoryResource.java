package dtb.lego.truck.rest.data.events.acquisition.boundary;

import dtb.lego.truck.rest.component.events.entity.events.Event;
import dtb.lego.truck.rest.data.events.acquisition.control.SensorDataHandler;
import dtb.lego.truck.rest.errors.InputValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Collection;

@RestController
@Api(description = "Operations pertaining the acquisition of historical data of the events")
public class EventHistoryResource {

    private SensorDataHandler sensorDataHandler;
    private InputValidator inputValidator;

    @Autowired
    public EventHistoryResource(SensorDataHandler sensorDataHandler, InputValidator inputValidator) {
        this.sensorDataHandler = sensorDataHandler;
        this.inputValidator = inputValidator;
    }

    @ApiOperation(value = "Get all the events from the component contained in the requested interval." +
            "Set 0 on end to use the current time instead")
    @GetMapping("/events/history")
    public ResponseEntity<Collection<? extends Event>> getComponentEventHistoryInInterval(@RequestParam String component,
                                                                                          @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from,
                                                                                          @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to) {
        long begin = from.toInstant().toEpochMilli();
        long end = to.toInstant().toEpochMilli();
        inputValidator.checkValidComponent(component);
        inputValidator.checkThatBeginIsBeforeEnd(begin, end);

        Collection<? extends Event> events = sensorDataHandler.getEventsInInterval(component, begin, end);
        return ResponseEntity.ok()
                .body(events);
    }

    //2019-08-06T13:55:31.016+01:00
    @ApiOperation(value = "Get all the events from the component contained in the requested interval, with the sampling rate requested" +
            " and applying the required transformation")
    @GetMapping("/events/history/transformed")
    public ResponseEntity<Collection<? extends Event>> getTransformedComponentEventHistoryInInterval(@RequestParam String component,
                                                                                                     @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from,
                                                                                                     @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to,
                                                                                                     @RequestParam int sampling,
                                                                                                     @RequestParam String transformation,
                                                                                                     @RequestParam(defaultValue = "false") boolean fillGaps) {
        long begin = from.toInstant().toEpochMilli();
        long end = to.toInstant().toEpochMilli();
        inputValidator.checkValidComponent(component);
        inputValidator.checkThatBeginIsBeforeEnd(begin, end);
        inputValidator.checkBeginTransformedHistoryLessThan1MonthAgo(begin);
        inputValidator.checkValidTransformation(transformation);

        Collection<? extends Event> events = sensorDataHandler.getTransformedEventHistory(sampling, component, transformation, begin, end, fillGaps);
        return ResponseEntity.ok()
                .body(events);
    }

    @DeleteMapping("/events/history")
    public ResponseEntity<String> deleteEventHistoryFromComponent(@RequestParam String component,
                                                                  @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from,
                                                                  @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to) {
        long begin = from.toInstant().toEpochMilli();
        long end = to.toInstant().toEpochMilli();
        inputValidator.checkValidComponent(component);
        inputValidator.checkThatBeginIsBeforeEnd(begin, end);

        sensorDataHandler.deleteEventsInInterval(component, begin, end);
        return ResponseEntity.ok().body("Deleted");
    }


}
