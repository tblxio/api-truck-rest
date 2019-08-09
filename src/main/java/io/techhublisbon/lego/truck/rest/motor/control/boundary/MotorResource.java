package io.techhublisbon.lego.truck.rest.motor.control.boundary;

import io.techhublisbon.lego.truck.rest.motor.control.control.MotorDriveHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller to expose endpoints related to the SBrick controller, namely
 * it provides control over the LegoTrucks motors as well as information on the
 * battery state and temperature of the controller
 */
@RestController
@Api(description = "Operations pertaining the control and operation of the Lego truck motors")
public class MotorResource {


    private MotorDriveHandler myMotorDriveHandler;

    @Autowired
    public MotorResource(MotorDriveHandler myMotorDriveHandler) {
        this.myMotorDriveHandler = myMotorDriveHandler;
    }

    @ApiOperation(value = "Sends a drive command to the requested motor with the respective power ")
    @PostMapping("motors/drive")
    public ResponseEntity<String> drive(
            @RequestParam String motion,
            @RequestParam(name = "power", defaultValue = "0") int power) {
        System.out.println("[" + System.currentTimeMillis() + "] Drive + " + motion + " with power " + power + " command received");
        myMotorDriveHandler.drive(motion, power);
        return new ResponseEntity<>("Drive command sent", HttpStatus.OK);
    }


}