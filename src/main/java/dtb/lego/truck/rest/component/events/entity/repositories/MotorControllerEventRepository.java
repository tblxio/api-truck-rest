package dtb.lego.truck.rest.component.events.entity.repositories;

import dtb.lego.truck.rest.component.events.entity.events.MotorControllerEvent;

import javax.transaction.Transactional;

/**
 * CrudRepository extension for motor controller Events.
 * Used in order to store the motor controller events on the database
 */
@Transactional
public interface MotorControllerEventRepository extends EventRepository<MotorControllerEvent> {
}
