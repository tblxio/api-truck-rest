package io.techhublisbon.lego.truck.rest.components.control;

import io.techhublisbon.lego.truck.rest.components.entity.events.xyz.sensor.GyroscopeEvent;

import javax.transaction.Transactional;

/**
 * CrudRepository extension for Gyroscope Events.
 * Used in order to store the gyroscope events on the database
 */
@Transactional
public interface GyroscopeEventRepository extends XYZSensorEventRepository<GyroscopeEvent> {
}
