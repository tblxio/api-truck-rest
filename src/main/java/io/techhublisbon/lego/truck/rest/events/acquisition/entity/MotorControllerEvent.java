package io.techhublisbon.lego.truck.rest.events.acquisition.entity;

import io.techhublisbon.lego.truck.rest.components.entity.Component;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Represents an data acquisition event from the SBrick,
 * which contains information regarding the battery and temperature of the
 * controller and implements the Event interface
 */
@Getter
@Setter
@Entity(name = "motor")
@Table(name = "motor")
public class MotorControllerEvent extends Event {
    private static final Logger LOG = LoggerFactory.getLogger(MotorControllerEvent.class);
    /**
     * The fields contain the timestamp, which is unique between
     * events and it is used as the primary key for the DB storage,
     * the battery voltage and temperature of the motor controller, in our case
     * the SBrick.
     */
    private double batVoltage;
    private double temperature;


    public MotorControllerEvent(long timestamp, Object voltage, Object temperature) {
        super(timestamp);
        this.batVoltage = (Double) voltage;
        this.temperature = (Double) temperature;
        this.name = Component.MOTOR;
    }

    public MotorControllerEvent() {
        super();
    }

    @Override
    public String toString() {
        return "MotorControllerEvent{" + "batVoltage=" + batVoltage + ", temperature=" + temperature + ", timestamp=" + timestamp + ", name=" + name + '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MotorControllerEvent)) return false;
        MotorControllerEvent that = (MotorControllerEvent) o;
        return Double.compare(that.getBatVoltage(), getBatVoltage()) == 0 && Double.compare(that.getTemperature(), getTemperature()) == 0 && this.getName() == that.getName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBatVoltage(), getTemperature(), getName());
    }
}
