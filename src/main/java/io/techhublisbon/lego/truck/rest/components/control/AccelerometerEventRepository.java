package io.techhublisbon.lego.truck.rest.components.control;

import io.techhublisbon.lego.truck.rest.components.entity.events.xyz.sensor.AccelerometerEvent;

import javax.transaction.Transactional;

/**
 * CrudRepository extension for Accelerometer Events.
 * Used in order to store the accelerometer events on the database
 */
@Transactional
public interface AccelerometerEventRepository extends XYZSensorEventRepository<AccelerometerEvent> {
}
