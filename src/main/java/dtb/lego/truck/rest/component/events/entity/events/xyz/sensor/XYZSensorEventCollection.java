package dtb.lego.truck.rest.component.events.entity.events.xyz.sensor;

import dtb.lego.truck.rest.component.events.entity.Components;
import dtb.lego.truck.rest.data.events.acquisition.entity.LegoTruckException;
import dtb.lego.truck.rest.errors.Errors;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class contain a collection of the XYZ events from
 * the different sensors, it is used to store the events received
 * until the pre-processing and storage is done
 */
public class XYZSensorEventCollection {

    /**
     * The fields contain the collections holding the events.
     * The use of Thread-Safe Arrays here is due to the multi-threaded
     * nature of the message handling, which can be storing a message while
     * another thread can be reading the array in order to perform the
     * database storage
     */
    private CopyOnWriteArrayList<AccelerometerEvent> accelerometerEvents;
    private CopyOnWriteArrayList<GyroscopeEvent> gyroscopeEvents;
    private GyroscopeEvent lastEventG; // Mudar para a BD
    private AccelerometerEvent lastEventA;

    public XYZSensorEventCollection(CopyOnWriteArrayList<AccelerometerEvent> accelerometerEvents,
                                    CopyOnWriteArrayList<GyroscopeEvent> gyroscopeEvents) {
        this.accelerometerEvents = accelerometerEvents;
        this.gyroscopeEvents = gyroscopeEvents;
    }

    /**
     * Get the Accelerometer Events
     *
     * @return The Accelerometer events collection
     */
    public Collection<AccelerometerEvent> getAccelerometerEvents() {
        return accelerometerEvents;
    }

    /**
     * Get the Accelerometer Events
     *
     * @return The Gyroscope events collection
     */
    public Collection<GyroscopeEvent> getGyroscopeEvents() {
        return gyroscopeEvents;
    }

    /**
     * Get the last event from the sensor
     *
     * @param sensor -- The requested sensor
     *               Should be part of the XYZSensor enum
     * @return The last event of that sensor
     */
    public XYZSensorEvent getLast(Components sensor) {
        switch (sensor) {
            case GYROSCOPE:
                if (gyroscopeEvents.size() > 0) return gyroscopeEvents.get(gyroscopeEvents.size() - 1);
            case ACCELEROMETER:
                if (accelerometerEvents.size() > 0) return accelerometerEvents.get(accelerometerEvents.size() - 1);
        }
        throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, sensor);
    }

    /**
     * Get the first event from the sensor
     *
     * @param sensor -- The requested sensor
     *               Should be part of the XYZSensor enum
     * @return The first event of that sensor
     */
    public XYZSensorEvent getFirst(Components sensor) {
        switch (sensor) {
            case GYROSCOPE:
                return gyroscopeEvents.get(0);
            case ACCELEROMETER:
                return accelerometerEvents.get(0);
        }
        return null;
    }

    /**
     * Clear the collection of events from the sensor
     *
     * @param sensor -- The requested sensor
     *               Should be part of the XYZSensor enum
     */
    public void clearList(Components sensor) {
        switch (sensor) {
            case GYROSCOPE:
                gyroscopeEvents.clear();
            case ACCELEROMETER:
                accelerometerEvents.clear();
        }
    }

    /**
     * Clear all the collections of events
     */
    public void clearAll() {
        clearList(Components.GYROSCOPE);
        clearList(Components.ACCELEROMETER);
    }
}
