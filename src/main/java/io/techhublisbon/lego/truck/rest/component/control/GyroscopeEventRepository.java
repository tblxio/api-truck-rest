package io.techhublisbon.lego.truck.rest.component.control;

import io.techhublisbon.lego.truck.rest.component.entity.events.xyz.sensor.GyroscopeEvent;

import javax.transaction.Transactional;

/**
 * CrudRepository extension for Gyroscope Events.
 * Used in order to store the gyroscope events on the database
 */
@Transactional
public interface GyroscopeEventRepository extends XYZSensorEventRepository<GyroscopeEvent> {
}
