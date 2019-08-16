package io.techhublisbon.lego.truck.rest.events.acquisition.control;

import io.techhublisbon.lego.truck.rest.components.control.AccelerometerEventRepository;
import io.techhublisbon.lego.truck.rest.components.control.GyroscopeEventRepository;
import io.techhublisbon.lego.truck.rest.components.control.MotorControllerEventRepository;
import io.techhublisbon.lego.truck.rest.components.control.ProximitySensorEventRepository;
import io.techhublisbon.lego.truck.rest.components.entity.Component;
import io.techhublisbon.lego.truck.rest.errors.Errors;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * This class contains all the operations regarding the database interaction, therefore, in case of the addition of new
 * functionality it should be extended.
 */
@org.springframework.stereotype.Component
public class DatabaseHandler {


    private AccelerometerEventRepository accelerometerEventRepository;
    private GyroscopeEventRepository gyroscopeEventRepository;
    private MotorControllerEventRepository motorControllerEventRepository;
    private ProximitySensorEventRepository proximitySensorEventRepository;

    @Autowired
    public DatabaseHandler(AccelerometerEventRepository accelerometerEventRepository, GyroscopeEventRepository gyroscopeEventRepository, MotorControllerEventRepository motorControllerEventRepository, ProximitySensorEventRepository proximitySensorEventRepository) {
        this.accelerometerEventRepository = accelerometerEventRepository;
        this.gyroscopeEventRepository = gyroscopeEventRepository;
        this.motorControllerEventRepository = motorControllerEventRepository;
        this.proximitySensorEventRepository = proximitySensorEventRepository;
    }


    public void saveXYZSensorEvent(AccelerometerEvent accelerometerEvent, GyroscopeEvent gyroscopeEvent) {
        accelerometerEventRepository.save(accelerometerEvent);
        gyroscopeEventRepository.save(gyroscopeEvent);
    }


    public void saveMotorControllerEvent(MotorControllerEvent motorControllerEvent) {
        motorControllerEventRepository.save(motorControllerEvent);
    }

    public void saveProximitySensorEvent(ProximitySensorEvent proximitySensorEvent) {
        proximitySensorEventRepository.save(proximitySensorEvent);
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

    Collection<ProximitySensorEvent> getProximityEventsInInterval(long begin, long end) {
        return proximitySensorEventRepository.findEventsInInterval(begin, end);
    }

    Collection<GyroscopeEvent> getGyroscopeMeanEventsInInterval(long begin, long end, long interval, boolean fillGaps) {
        if (fillGaps) return gyroscopeEventRepository.findAvgEventsInIntervalFillingGaps(begin, end, interval);
        return gyroscopeEventRepository.findAvgEventsInInterval(begin, end, interval);
    }

    Collection<GyroscopeEvent> getGyroscopeMaxEventsInInterval(long begin, long end, long interval, boolean fillGaps) {
        if (fillGaps) return gyroscopeEventRepository.findMaxEventsInIntervalFillingGaps(begin, end, interval);
        return gyroscopeEventRepository.findMaxEventsInInterval(begin, end, interval);
    }

    Collection<GyroscopeEvent> getGyroscopeMinEventsInInterval(long begin, long end, long interval, boolean fillGaps) {
        if (fillGaps) return gyroscopeEventRepository.findMinEventsInIntervalFillingGaps(begin, end, interval);
        return gyroscopeEventRepository.findMinEventsInInterval(begin, end, interval);
    }

    Collection<GyroscopeEvent> getGyroscopeMedianEventsInInterval(long begin, long end, long interval, boolean fillGaps) {
        if (fillGaps) return gyroscopeEventRepository.findMedianEventsInIntervalFillingGaps(begin, end, interval);
        return gyroscopeEventRepository.findMedianEventsInInterval(begin, end, interval);
    }

    Collection<GyroscopeEvent> getGyroscopeLastEventsInInterval(long begin, long end, long interval, boolean fillGaps) {
        if (fillGaps) return gyroscopeEventRepository.findLastEventsInIntervalFillingGaps(begin, end, interval);
        return gyroscopeEventRepository.findLastEventsInInterval(begin, end, interval);
    }

    Collection<AccelerometerEvent> getAccelerometerMeanEventsInInterval(long begin, long end, long interval, boolean fillGaps) {
        if (fillGaps) return accelerometerEventRepository.findAvgEventsInIntervalFillingGaps(begin, end, interval);
        return accelerometerEventRepository.findAvgEventsInInterval(begin, end, interval);
    }

    Collection<AccelerometerEvent> getAccelerometerMaxEventsInInterval(long begin, long end, long interval, boolean fillGaps) {
        if (fillGaps) return accelerometerEventRepository.findMaxEventsInIntervalFillingGaps(begin, end, interval);
        return accelerometerEventRepository.findMaxEventsInInterval(begin, end, interval);
    }

    Collection<AccelerometerEvent> getAccelerometerMinEventsInInterval(long begin, long end, long interval, boolean fillGaps) {
        if (fillGaps) return accelerometerEventRepository.findMinEventsInIntervalFillingGaps(begin, end, interval);
        return accelerometerEventRepository.findMinEventsInInterval(begin, end, interval);
    }

    Collection<AccelerometerEvent> getAccelerometerMedianEventsInInterval(long begin, long end, long interval, boolean fillGaps) {
        if (fillGaps) return accelerometerEventRepository.findMedianEventsInIntervalFillingGaps(begin, end, interval);
        return accelerometerEventRepository.findMedianEventsInInterval(begin, end, interval);
    }

    Collection<AccelerometerEvent> getAccelerometerLastEventsInInterval(long begin, long end, long interval, boolean fillGaps) {
        if (fillGaps) return accelerometerEventRepository.findLastEventsInIntervalFillingGaps(begin, end, interval);
        return accelerometerEventRepository.findLastEventsInInterval(begin, end, interval);
    }

    Collection<MotorControllerEvent> getMotorControllerLastEventsInInterval(long begin, long end, long interval, boolean fillGaps) {
        if (fillGaps) return motorControllerEventRepository.findLastEventsInIntervalFillingGaps(begin, end, interval);
        return motorControllerEventRepository.findLastEventsInInterval(begin, end, interval);
    }

    Collection<ProximitySensorEvent> getProximityLastEventsInInterval(long begin, long end, long interval, boolean fillGaps) {
        if (fillGaps) return proximitySensorEventRepository.findLastEventsInIntervalFillingGaps(begin, end, interval);
        return proximitySensorEventRepository.findLastEventsInInterval(begin, end, interval);
    }

    XYZSensorEvent getXYZSensorMeanEventInLastInterval(long interval, Component component) {
        switch (component) {
            case GYROSCOPE:
                return gyroscopeEventRepository.findEventMeaninLastTimeUnits(interval);
            case ACCELEROMETER:
                return accelerometerEventRepository.findEventMeaninLastTimeUnits(interval);
            default:
                throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, component, "mean");

        }
    }

    XYZSensorEvent getXYZSensorMaxEventInLastInterval(long interval, Component component) {

        switch (component) {
            case GYROSCOPE:
                return gyroscopeEventRepository.findEventMaxinLastTimeUnits(interval);
            case ACCELEROMETER:
                return accelerometerEventRepository.findEventMaxinLastTimeUnits(interval);
            default:
                throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, component);
        }

    }

    XYZSensorEvent getXYZSensorMinEventInLastInterval(long interval, Component component) {

        switch (component) {
            case GYROSCOPE:
                return gyroscopeEventRepository.findEventMininLastTimeUnits(interval);
            case ACCELEROMETER:
                return accelerometerEventRepository.findEventMininLastTimeUnits(interval);
            default:
                throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, component);
        }

    }

    XYZSensorEvent getLastXYZSensorEvent(Component component) {
        switch (component) {
            case GYROSCOPE:
                return gyroscopeEventRepository.findLastEvent();
            case ACCELEROMETER:
                return accelerometerEventRepository.findLastEvent();
            default:
                throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, component);
        }
    }

    MotorControllerEvent getLastMotorControllerEvent() {
        return motorControllerEventRepository.findLastEvent();
    }

    ProximitySensorEvent getLastProximityEvent() {
        return proximitySensorEventRepository.findLastEvent();
    }

    void deleteEventsInInterval(Component component, long begin, long end) {
        switch (component) {
            case GYROSCOPE:
                gyroscopeEventRepository.deleteEventsInInterval(begin, end);
                break;
            case ACCELEROMETER:
                accelerometerEventRepository.deleteEventsInInterval(begin, end);
                break;
            case MOTOR:
                motorControllerEventRepository.deleteEventsInInterval(begin, end);
                break;
            case PROXIMITY:
                proximitySensorEventRepository.deleteEventsInInterval(begin, end);
                break;
            default:
                throw new LegoTruckException(Errors.INVALID_PARAMETER, component);
        }
    }

}
