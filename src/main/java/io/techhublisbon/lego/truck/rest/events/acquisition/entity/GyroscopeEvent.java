package io.techhublisbon.lego.truck.rest.events.acquisition.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Represents an gyroscope data event
 * This class extends XYZSensor event and only
 * every XYZ Sensor to have its own table on the database
 */
@Entity(name = "gyroscope")
@Table(name = "gyroscope")
public class GyroscopeEvent extends XYZSensorEvent {

    public GyroscopeEvent(Object timestamp, Object x, Object y, Object z, String name) {
        super(timestamp, x, y, z, name);
    }

    public GyroscopeEvent() {
    }
}
