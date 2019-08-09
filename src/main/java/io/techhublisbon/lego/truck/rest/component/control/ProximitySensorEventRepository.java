package io.techhublisbon.lego.truck.rest.component.control;

import io.techhublisbon.lego.truck.rest.component.entity.events.ProximitySensorEvent;

import javax.transaction.Transactional;

/**
 * CrudRepository extension for Gyroscope Events.
 * Used in order to store the gyroscope events on the database
 */
@Transactional
public interface ProximitySensorEventRepository extends EventRepository<ProximitySensorEvent> {
}
