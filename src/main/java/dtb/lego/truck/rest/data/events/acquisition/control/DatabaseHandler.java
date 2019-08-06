package dtb.lego.truck.rest.data.events.acquisition.control;

import dtb.lego.truck.rest.component.events.entity.Components;
import dtb.lego.truck.rest.component.events.entity.events.MotorControllerEvent;
import dtb.lego.truck.rest.component.events.entity.events.xyz.sensor.AccelerometerEvent;
import dtb.lego.truck.rest.component.events.entity.events.xyz.sensor.GyroscopeEvent;
import dtb.lego.truck.rest.component.events.entity.events.xyz.sensor.XYZSensorEvent;
import dtb.lego.truck.rest.component.events.entity.repositories.AccelerometerEventRepository;
import dtb.lego.truck.rest.component.events.entity.repositories.GyroscopeEventRepository;
import dtb.lego.truck.rest.component.events.entity.repositories.MotorControllerEventRepository;
import dtb.lego.truck.rest.data.events.acquisition.entity.LegoTruckException;
import dtb.lego.truck.rest.errors.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * This class contains all the operations regarding the database interaction, therefore, in case of the addition of new
 * functionality it should be extended.
 */
@Component
public class DatabaseHandler {

    @Autowired
    private AccelerometerEventRepository accelerometerEventRepository;

    @Autowired
    private GyroscopeEventRepository gyroscopeEventRepository;

    @Autowired
    private MotorControllerEventRepository motorControllerEventRepository;


    void saveXYZSensorEvents(Collection<AccelerometerEvent> accelerometerEvents,
                             Collection<GyroscopeEvent> gyroscopeEvents
    ) {
        accelerometerEventRepository.saveAll(accelerometerEvents);
        gyroscopeEventRepository.saveAll(gyroscopeEvents);
    }

    public void saveXYZSensorEvent(AccelerometerEvent accelerometerEvent,
                                   GyroscopeEvent gyroscopeEvent
    ) {
        accelerometerEventRepository.save(accelerometerEvent);
        gyroscopeEventRepository.save(gyroscopeEvent);
    }

    void saveMotorControllerEvents(Collection<MotorControllerEvent> motorControllerEvents) {
        motorControllerEventRepository.saveAll(motorControllerEvents);
    }

    public void saveMotorControllerEvent(MotorControllerEvent motorControllerEvent) {
        motorControllerEventRepository.save(motorControllerEvent);
    }

    Collection<GyroscopeEvent> getGyroscopeEventsInInterval(long begin, long end) {
        return gyroscopeEventRepository.findEventsInInterval(begin, end);
    }

    Collection<AccelerometerEvent> getAccelerometerEventsInInterval(long begin, long end) {
        return accelerometerEventRepository.findEventsInInterval(begin, end);
    }

    Collection<MotorControllerEvent> getMotorEventsInInterval(long begin, long end) {
        return motorControllerEventRepository.findEventsInInterval(begin, end);
    }

    Collection<GyroscopeEvent> getGyroscopeMeanEventsInInterval(long begin, long end, long interval) {
        return gyroscopeEventRepository.findAvgEventsInInterval(begin, end, interval);
    }

    Collection<GyroscopeEvent> getGyroscopeMaxEventsInInterval(long begin, long end, long interval) {
        return gyroscopeEventRepository.findMaxEventsInInterval(begin, end, interval);
    }

    Collection<GyroscopeEvent> getGyroscopeMinEventsInInterval(long begin, long end, long interval) {
        return gyroscopeEventRepository.findMinEventsInInterval(begin, end, interval);
    }

    Collection<AccelerometerEvent> getAccelerometerMeanEventsInInterval(long begin, long end, long interval) {
        return accelerometerEventRepository.findAvgEventsInInterval(begin, end, interval);
    }

    Collection<AccelerometerEvent> getAccelerometerMaxEventsInInterval(long begin, long end, long interval) {
        return accelerometerEventRepository.findMaxEventsInInterval(begin, end, interval);
    }

    Collection<AccelerometerEvent> getAccelerometerMinEventsInInterval(long begin, long end, long interval) {
        return accelerometerEventRepository.findMinEventsInInterval(begin, end, interval);
    }

    XYZSensorEvent getXYZSensorMeanEventInLastInterval(long interval, Components component) {
        switch (component) {
            case GYROSCOPE:
                return gyroscopeEventRepository.findEventMeaninLastTimeUnits(interval);
            case ACCELEROMETER:
                return accelerometerEventRepository.findEventMeaninLastTimeUnits(interval);
        }
        throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, component);
    }

    XYZSensorEvent getXYZSensorMaxEventInLastInterval(long interval, Components component) {

        switch (component) {
            case GYROSCOPE:
                return gyroscopeEventRepository.findEventMaxinLastTimeUnits(interval);
            case ACCELEROMETER:
                return accelerometerEventRepository.findEventMaxinLastTimeUnits(interval);
        }
        throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, component);

    }

    XYZSensorEvent getXYZSensorMinEventInLastInterval(long interval, Components component) {

        switch (component) {
            case GYROSCOPE:
                return gyroscopeEventRepository.findEventMininLastTimeUnits(interval);
            case ACCELEROMETER:
                return accelerometerEventRepository.findEventMininLastTimeUnits(interval);
        }
        throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, component);
    }

    XYZSensorEvent getLastXYZSensorEvent(Components component) {
        switch (component) {
            case GYROSCOPE:
                return gyroscopeEventRepository.findLastEvent();
            case ACCELEROMETER:
                return accelerometerEventRepository.findLastEvent();
        }
        throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, component);
    }

    MotorControllerEvent getLastMotorControllerEvent() {
        return motorControllerEventRepository.findLastEvent();
    }

    void deleteEventsInInterval(Components component, long begin, long end) {
        switch (component) {
            case GYROSCOPE:
                gyroscopeEventRepository.deleteEventsInInterval(begin, end);
            case ACCELEROMETER:
                accelerometerEventRepository.deleteEventsInInterval(begin, end);
            case MOTOR:
                motorControllerEventRepository.deleteEventsInInterval(begin, end);
        }
    }

}
