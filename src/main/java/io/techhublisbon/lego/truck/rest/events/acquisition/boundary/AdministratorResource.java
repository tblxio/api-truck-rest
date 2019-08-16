package io.techhublisbon.lego.truck.rest.events.acquisition.boundary;

import io.swagger.annotations.Api;
import io.techhublisbon.lego.truck.rest.errors.InputValidator;
import io.techhublisbon.lego.truck.rest.events.acquisition.control.EventDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@Api("Administration functions")
public class AdministratorResource {

    private InputValidator inputValidator;
    private EventDataHandler eventDataHandler;

    @Autowired
    public AdministratorResource(InputValidator inputValidator, EventDataHandler eventDataHandler) {
        this.inputValidator = inputValidator;
        this.eventDataHandler = eventDataHandler;
    }

    @DeleteMapping("admin/events/history")
    public ResponseEntity<String> deleteEventHistoryFromComponent(@RequestParam String component, @RequestParam(name = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime from, @RequestParam(name = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime to) {
        long begin = from.toInstant().toEpochMilli();
        long end = to.toInstant().toEpochMilli();
        inputValidator.checkValidComponent(component);
        inputValidator.checkThatBeginIsBeforeEnd(begin, end);

        eventDataHandler.deleteEventsInInterval(component, begin, end);
        return ResponseEntity.ok().body("Deleted");
    }
}
