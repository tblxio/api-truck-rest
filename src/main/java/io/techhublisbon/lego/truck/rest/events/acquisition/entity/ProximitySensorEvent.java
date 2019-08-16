package io.techhublisbon.lego.truck.rest.events.acquisition.entity;

import io.techhublisbon.lego.truck.rest.components.entity.Component;
import lombok.Getter;
import lombok.Setter;

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
@Entity(name = "proximity")
@Table(name = "proximity")
public class ProximitySensorEvent extends Event {
    /**
     * The fields contain the timestamp, which is unique between
     * events and it is used as the primary key for the DB storage,
     * the battery voltage and temperature of the motor controller, in our case
     * the SBrick.
     */
    private double distance;


    public ProximitySensorEvent(Object timestamp, Object distance) {
        super((long) timestamp);
        this.distance = (Double) distance;
        this.name = Component.PROXIMITY;
    }

    public ProximitySensorEvent() {
        super();
    }


    @Override
    public String toString() {
        return "ProximitySensorEvent{" + "distance=" + distance + ", name=" + name + ", timestamp=" + timestamp + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProximitySensorEvent)) return false;
        ProximitySensorEvent that = (ProximitySensorEvent) o;
        return Double.compare(that.getDistance(), getDistance()) == 0 && getName() == that.getName();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDistance(), getName());
    }
}
