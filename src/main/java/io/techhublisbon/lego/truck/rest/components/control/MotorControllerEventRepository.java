package io.techhublisbon.lego.truck.rest.components.control;

import io.techhublisbon.lego.truck.rest.components.entity.events.MotorControllerEvent;

import javax.transaction.Transactional;

/**
 * CrudRepository extension for motor controller Events.
 * Used in order to store the motor controller events on the database
 */
@Transactional
public interface MotorControllerEventRepository extends EventRepository<MotorControllerEvent> {
}
