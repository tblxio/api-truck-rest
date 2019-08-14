package io.techhublisbon.lego.truck.rest.events.acquisition.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Represents an accelerometer data event
 * This class extends XYZSensor event and only
 * every XYZ Sensor to have its own table on the database
 */
@Entity(name = "accelerometer")
@Table(name = "accelerometer")
public class AccelerometerEvent extends XYZSensorEvent {


    public AccelerometerEvent(Object timestamp, Object x, Object y, Object z) {
        super(timestamp, x, y, z, "accelerometer");
    }

    public AccelerometerEvent() {
    }
}
