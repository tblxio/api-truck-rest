package dtb.lego.truck.rest.data.events.acquisition.control;

import dtb.lego.truck.rest.MqttHandler;
import dtb.lego.truck.rest.component.events.entity.ComponentInfo;
import dtb.lego.truck.rest.component.events.entity.ComponentInfoCollection;
import dtb.lego.truck.rest.component.events.entity.Components;
import dtb.lego.truck.rest.component.events.entity.Transformations;
import dtb.lego.truck.rest.component.events.entity.events.Event;
import dtb.lego.truck.rest.component.events.entity.events.MotorControllerEvent;
import dtb.lego.truck.rest.component.events.entity.events.xyz.sensor.XYZSensorEventCollection;
import dtb.lego.truck.rest.data.events.acquisition.entity.LegoTruckException;
import dtb.lego.truck.rest.data.events.acquisition.entity.MqttDataCallback;
import dtb.lego.truck.rest.errors.Errors;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class handles all the operations pertaining to data acquisition from the sensors. It is as a middleman between the
 * data received via MQTT and the database, as well as between the RestControllers and the Database.
 */
@Component
@DependsOn("MqttHandler")
public class SensorDataHandler {

    private final int motorDBSaveInterval = 60000;
    @Autowired
    MotorDataHandler motorDataHandler;
    @Autowired
    private MqttHandler myMqttHandler;
    @Autowired
    private DatabaseHandler databaseHandler;
    private XYZSensorEventCollection xyzSensorEventCollection;
    private ComponentInfoCollection componentInfo;
    private CopyOnWriteArrayList<MotorControllerEvent> motorControllerEvents;

    /**
     * Performs all the setup operations after the construction of the Bean, like subscribing to the Mqtt topics, initializing
     * the local Collections, setting the MqttCallback and manually adding the Motor Controller information to the components list
     */
    @PostConstruct
    public void setupDataHandler() {
        try {
            myMqttHandler.subscribe("truck1/#");
        } catch (MqttException e) {
            MqttHandler.handle_execp_gracefully(e);
        }
        componentInfo = new ComponentInfoCollection(new CopyOnWriteArrayList<>());
        xyzSensorEventCollection = new XYZSensorEventCollection(new CopyOnWriteArrayList<>(),
                new CopyOnWriteArrayList<>());
        motorControllerEvents = new CopyOnWriteArrayList<>();
        // The SBrick does not send its information so we need to add it manually
        componentInfo.getComponentInfos().add(new ComponentInfo("motor", 20000.0));
        myMqttHandler.setCallback(new MqttDataCallback(componentInfo,
                xyzSensorEventCollection, motorControllerEvents, databaseHandler));
    }

    /**
     * Returns the requested event data, applying the requested transformation over the data in interval specified.
     * Used for operations over the last X time units.
     *
     * @param interval  The interval to perform the transformation on the data, starting in the current timestamp and
     *                  going back
     * @param comp      The component requested (motor,gyroscope,etc..)
     * @param transform The transformation to be applied to downsample the data
     */
    public Event getTransformedEvent(long interval, String comp, String transform) throws IllegalArgumentException, LegoTruckException {
        try {
            Components component = Components.valueOf(comp.toUpperCase());
            Transformations transformation = Transformations.valueOf(transform.toUpperCase());
            switch (transformation) {
                case MAX:
                    if (component == Components.MOTOR)
                        throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, transformation, "Only last allowed for Motor");
                    return maxEvent(interval, component);
                case MEAN:
                    if (component == Components.MOTOR)
                        throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, transformation, "Only last allowed for Motor");
                    return meanEvent(interval, component);
                case MIN:
                    if (component == Components.MOTOR)
                        throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, transformation, "Only last allowed for Motor");
                    return minEvent(interval, component);
                case LAST:
                    return getLastEvent(component);
            }
        } catch (NullPointerException np) {
            throw new LegoTruckException(Errors.RESOURCE_EMPTY, comp, transform);
        }
        throw new LegoTruckException(Errors.INVALID_PARAMETER);
    }

    /**
     * Returns the the last event from the component
     *
     * @param component The component requested (motor,gyroscope,etc..)
     */
    private Event getLastEvent(Components component) {
        if (component == Components.MOTOR) return databaseHandler.getLastMotorControllerEvent();
        return databaseHandler.getLastXYZSensorEvent(component);
    }

    /**
     * Returns the the avg value event from the component in the last X time units
     *
     * @param component The component requested (motor,gyroscope,etc..)
     */
    private Event meanEvent(long interval, Components component) {
        interval = interval * 1000; //Transform into microsencods
        return databaseHandler.getXYZSensorMeanEventInLastInterval(interval, component);
    }

    /**
     * Returns the the max value event from the component in the last X time units
     *
     * @param component The component requested (motor,gyroscope,etc..)
     */
    private Event maxEvent(long interval, Components component) {
        interval = interval * 1000; //Transform into microsencods
        return databaseHandler.getXYZSensorMaxEventInLastInterval(interval, component);
    }

    /**
     * Returns the the min value event from the component in the last X time units
     *
     * @param component The component requested (motor,gyroscope,etc..)
     */
    private Event minEvent(long interval, Components component) {
        interval = interval * 1000; //Transform into microsencods
        return databaseHandler.getXYZSensorMinEventInLastInterval(interval, component);
    }

    /**
     * Returns the the information of the requested component
     *
     * @param component The component requested (motor,gyroscope,etc..)
     */
    public ComponentInfo getComponentInfo(Components component) {
        return componentInfo.getComponentInfo(component);
    }

    /**
     * Returns a collection containning the events in the given interval
     *
     * @param begin     The beginning of the interval
     * @param end       The end of the interval
     * @param component The component requested (motor,gyroscope,etc..)
     */
    public Collection<? extends Event> getEventsInInterval(String component, long begin, long end) {
        Components component1 = Components.valueOf(component.toUpperCase());
        switch (component1) {
            case GYROSCOPE:
                return databaseHandler.getGyroscopeEventsInInterval(begin, end);
            case ACCELEROMETER:
                return databaseHandler.getAccelerometerEventsInInterval(begin, end);
            case MOTOR:
                return databaseHandler.getMotorEventsInInterval(begin, end);
        }
        throw new LegoTruckException(Errors.INVALID_PARAMETER, component);
    }

    /**
     * Delete the events from a given component in a given interval
     *
     * @param begin     The beginning of the interval
     * @param end       The end of the interval
     * @param component The component requested (motor,gyroscope,etc..)
     */
    public void deleteEventsInInterval(String component, long begin, long end) {
        Components component1 = Components.valueOf(component.toUpperCase());
        databaseHandler.deleteEventsInInterval(component1, begin, end);
    }

    /**
     * Returns the requested events data in the time interval defined, applying the requested transformation over the data
     * in order to downsample it to the sampleInterval.
     *
     * @param sampleInterval The interval to perform the transformation on the data, starting in the current timestamp and
     *                       going back
     * @param comp           The component requested (motor,gyroscope,etc..)
     * @param transform      The transformation to be applied to downsample the data
     * @param begin          The beginning of the interval
     * @param end            The end of the interval
     */
    public Collection<? extends Event> getTransformedEventHistory(long sampleInterval, String comp, String transform, long begin,
                                                                  long end) throws IllegalArgumentException, LegoTruckException {
        sampleInterval *= 1000; //Convert to microsseconds
        try {
            Components component = Components.valueOf(comp.toUpperCase());

            if (component == Components.MOTOR)
                throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, "Historic Transformations not implemented for the motor");
            Transformations transformation = Transformations.valueOf(transform.toUpperCase());

            switch (transformation) {
                case MAX:
                    if (component == Components.GYROSCOPE)
                        return databaseHandler.getGyroscopeMaxEventsInInterval(begin, end, sampleInterval);
                    if (component == Components.ACCELEROMETER)
                        return databaseHandler.getAccelerometerMaxEventsInInterval(begin, end, sampleInterval);
                case MEAN:
                    if (component == Components.GYROSCOPE)
                        return databaseHandler.getGyroscopeMeanEventsInInterval(begin, end, sampleInterval);
                    if (component == Components.ACCELEROMETER)
                        return databaseHandler.getAccelerometerMeanEventsInInterval(begin, end, sampleInterval);

                case MIN:
                    if (component == Components.GYROSCOPE)
                        return databaseHandler.getGyroscopeMinEventsInInterval(begin, end, sampleInterval);
                    if (component == Components.ACCELEROMETER)
                        return databaseHandler.getAccelerometerMinEventsInInterval(begin, end, sampleInterval);
            }
        } catch (NullPointerException | IllegalArgumentException np) {
            throw new LegoTruckException(Errors.RESOURCE_EMPTY, comp, transform);
        }
        throw new LegoTruckException(Errors.INVALID_PARAMETER);
    }
}




