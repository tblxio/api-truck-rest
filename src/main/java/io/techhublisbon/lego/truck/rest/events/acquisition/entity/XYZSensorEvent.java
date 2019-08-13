package io.techhublisbon.lego.truck.rest.events.acquisition.entity;

import io.techhublisbon.lego.truck.rest.components.entity.Component;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.Objects;

/**
 * Represents an event from a 3 axis sensor,
 * such as the gyroscope and the accelerometer and implements
 * the Event interface
 */
@Getter
@Setter
@NoRepositoryBean
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class XYZSensorEvent extends Event {

    /**
     * The fields contain the timestamp, which is unique between
     * events and it is used as the primary key for the DB storage,
     * the x,y,z values of the measurement and the name of the sensor
     */
    private Double x;
    private Double y;
    private Double z;
    private Component name;

    public XYZSensorEvent(Object timestamp, Object x, Object y, Object z, String name) {
        super((long) timestamp);
        this.x = (double) x;
        this.y = (double) y;
        this.z = (double) z;
        this.name = Component.valueOf(name.toUpperCase());
    }

    public XYZSensorEvent() {
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
            payload.put("x", x);
            payload.put("y", y);
            payload.put("z", z);
            payload.put("timestamp", super.getTimestamp());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XYZSensorEvent xyzSensorEvent = (XYZSensorEvent) o;
        return super.getTimestamp() == xyzSensorEvent.getTimestamp() && Double.compare(xyzSensorEvent.x, x) == 0 && Double.compare(xyzSensorEvent.y, y) == 0 && Double.compare(xyzSensorEvent.z, z) == 0 && Objects.equals(name, xyzSensorEvent.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getTimestamp(), x, y, z, name);
    }

    @Override
    public String toString() {
        return "XYZSensorEvent{" + "timestamp=" + super.getTimestamp() + ", x=" + x + ", y=" + y + ", z=" + z + ", name=" + name + '}';
    }
}

