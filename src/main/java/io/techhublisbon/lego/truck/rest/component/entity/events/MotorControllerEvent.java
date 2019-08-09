package io.techhublisbon.lego.truck.rest.component.entity.events;

import io.techhublisbon.lego.truck.rest.component.entity.Component;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Table;

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
    /**
     * The fields contain the timestamp, which is unique between
     * events and it is used as the primary key for the DB storage,
     * the battery voltage and temperature of the motor controller, in our case
     * the SBrick.
     */
    private double batVoltage;
    private double temperature;
    private Component name;

    public MotorControllerEvent(long timestamp, Object voltage, Object temperature, String name) {
        super(timestamp);
        this.batVoltage = (Double) voltage;
        this.temperature = (Double) temperature;
        this.name = Component.valueOf(name.toUpperCase());
    }

    public MotorControllerEvent() {
        super();
    }

    @Override
    public String toString() {
        return "MotorControllerEvent{" +
                "batVoltage=" + batVoltage +
                ", temperature=" + temperature +
                ", timestamp=" + timestamp +
                ", name=" + name +
                '}';
    }

    /**
     * Implementation of the toJson function from the interface
     * for this particular type of sensor event
     *
     * @return The Json form of the event
     */
    public JSONObject toJson() {
        JSONObject payload = new JSONObject();
        try {
            payload.put("name", name);
            payload.put("voltage", batVoltage);
            payload.put("temperature", temperature);
            payload.put("timestamp", super.getTimestamp());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return payload;
    }
}
