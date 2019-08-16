package io.techhublisbon.lego.truck.rest.events.acquisition.control;

import io.techhublisbon.lego.truck.rest.MqttHandler;
import io.techhublisbon.lego.truck.rest.components.entity.Component;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfo;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfoCollection;
import io.techhublisbon.lego.truck.rest.components.entity.Transformation;
import io.techhublisbon.lego.truck.rest.errors.Errors;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.Event;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.LegoTruckException;
import lombok.Getter;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * This class handles all the operations pertaining to data acquisition from the sensors. It is as a middleman between the
 * data received via MQTT and the database, as well as between the RestControllers and the Database.
 */
@org.springframework.stereotype.Component
@DependsOn("MqttHandler")
@Getter
public class EventDataHandler {

    private MqttHandler myMqttHandler;

    private DatabaseHandler databaseHandler;

    private ComponentInfoCollection componentInfo;

    @Autowired
    public EventDataHandler(MqttHandler myMqttHandler, DatabaseHandler databaseHandler, ComponentInfoCollection componentInfo) {
        this.myMqttHandler = myMqttHandler;
        this.databaseHandler = databaseHandler;
        this.componentInfo = componentInfo;
    }

    /**
     * Performs all the setup operations after the construction of the Bean, like subscribing to the Mqtt topics, initializing
     * the local Collections, setting the MqttCallback and manually adding the Motor Controller information to the components list
     */
    @PostConstruct
    public void setupDataHandler() {
        try {
            myMqttHandler.subscribe("truck1/#");
        } catch (MqttException e) {
            MqttHandler.handleGracefully(e);
        }
        // The SBrick does not send its information so we need to add it manually
        myMqttHandler.setCallback(new MqttDataCallback(componentInfo, databaseHandler));
    }

    /**
     * Returns the requested event data, applying the requested transformation over the data in interval specified.
     * Used for operations over the last X time units.
     *
     * @param interval             The interval to perform the transformation on the data, starting in the current timestamp and
     *                             going back
     * @param componentString      The components requested (motor,gyroscope,etc..)
     * @param transformationString The transformation to be applied to downsample the data
     */
    public Event getTransformedEvent(long interval, String componentString, String transformationString) {
        try {
            Component component = Component.valueOf(componentString.toUpperCase());
            Transformation transformation = Transformation.valueOf(transformationString.toUpperCase());
            switch (transformation) {
                case MAX:
                    if (component == Component.MOTOR || component == Component.PROXIMITY) break;
                    return maxEvent(interval, component);
                case MEAN:
                    if (component == Component.MOTOR || component == Component.PROXIMITY) break;
                    return meanEvent(interval, component);
                case MIN:
                    if (component == Component.MOTOR || component == Component.PROXIMITY) break;
                    return minEvent(interval, component);
                case LAST:
                    return getLastEvent(component);
                default:
                    throw new LegoTruckException(Errors.INVALID_PARAMETER, transformationString);
            }
        } catch (NullPointerException np) {
            throw new LegoTruckException(Errors.RESOURCE_EMPTY, componentString, transformationString);
        }
        throw new LegoTruckException(Errors.INVALID_PARAMETER, transformationString);
    }

    /**
     * Returns the the last event from the components
     *
     * @param component The components requested (motor,gyroscope,etc..)
     */
    private Event getLastEvent(Component component) {
        if (component == Component.MOTOR) return databaseHandler.getLastMotorControllerEvent();
        if (component == Component.PROXIMITY) return databaseHandler.getLastProximityEvent();
        return databaseHandler.getLastXYZSensorEvent(component);
    }

    /**
     * Returns the the avg value event from the components in the last X time units
     *
     * @param component The components requested (motor,gyroscope,etc..)
     */
    private Event meanEvent(long interval, Component component) {
        return databaseHandler.getXYZSensorMeanEventInLastInterval(interval, component);
    }

    /**
     * Returns the the max value event from the components in the last X time units
     *
     * @param component The components requested (motor,gyroscope,etc..)
     */
    private Event maxEvent(long interval, Component component) {
        return databaseHandler.getXYZSensorMaxEventInLastInterval(interval, component);
    }

    /**
     * Returns the the min value event from the components in the last X time units
     *
     * @param component The components requested (motor,gyroscope,etc..)
     */
    private Event minEvent(long interval, Component component) {
        return databaseHandler.getXYZSensorMinEventInLastInterval(interval, component);
    }

    /**
     * Returns the the information of the requested components
     *
     * @param component The components requested (motor,gyroscope,etc..)
     */
    public ComponentInfo getComponentInfo(Component component) {
        return componentInfo.getComponentInfo(component);
    }

    /**
     * Returns a collection containning the events in the given interval
     *
     * @param begin     The beginning of the interval
     * @param end       The end of the interval
     * @param component The components requested (motor,gyroscope,etc..)
     */
    public Collection<? extends Event> getEventsInInterval(String component, long begin, long end) {
        Component component1 = Component.valueOf(component.toUpperCase());
        switch (component1) {
            case GYROSCOPE:
                return databaseHandler.getGyroscopeEventsInInterval(begin, end);
            case ACCELEROMETER:
                return databaseHandler.getAccelerometerEventsInInterval(begin, end);
            case MOTOR:
                return databaseHandler.getMotorEventsInInterval(begin, end);
            case PROXIMITY:
                return databaseHandler.getProximityEventsInInterval(begin, end);
            default:
                throw new LegoTruckException(Errors.INVALID_PARAMETER, component);
        }
    }

    /**
     * Delete the events from a given components in a given interval
     *
     * @param begin     The beginning of the interval
     * @param end       The end of the interval
     * @param component The components requested (motor,gyroscope,etc..)
     */
    public void deleteEventsInInterval(String component, long begin, long end) {
        Component component1 = Component.valueOf(component.toUpperCase());
        databaseHandler.deleteEventsInInterval(component1, begin, end);
    }

    /**
     * Returns the requested events data in the time interval defined, applying the requested transformation over the data
     * in order to downsample it to the sampleInterval.
     *
     * @param sampleInterval The interval to perform the transformation on the data, starting in the current timestamp and
     *                       going back
     * @param comp           The components requested (motor,gyroscope,etc..)
     * @param transform      The transformation to be applied to downsample the data
     * @param begin          The beginning of the interval
     * @param end            The end of the interval
     */
    public Collection<? extends Event> getTransformedEventHistory(long sampleInterval, String comp, String transform, long begin, long end, boolean fillGaps) {
        try {
            Component component = Component.valueOf(comp.toUpperCase());
            Transformation transformation = Transformation.valueOf(transform.toUpperCase());

            switch (transformation) {
                case MAX:
                    if (component == Component.GYROSCOPE)
                        return databaseHandler.getGyroscopeMaxEventsInInterval(begin, end, sampleInterval, fillGaps);
                    if (component == Component.ACCELEROMETER)
                        return databaseHandler.getAccelerometerMaxEventsInInterval(begin, end, sampleInterval, fillGaps);
                    break;
                case MEAN:
                    if (component == Component.GYROSCOPE)
                        return databaseHandler.getGyroscopeMeanEventsInInterval(begin, end, sampleInterval, fillGaps);
                    if (component == Component.ACCELEROMETER)
                        return databaseHandler.getAccelerometerMeanEventsInInterval(begin, end, sampleInterval, fillGaps);
                    break;
                case MIN:
                    if (component == Component.GYROSCOPE)
                        return databaseHandler.getGyroscopeMinEventsInInterval(begin, end, sampleInterval, fillGaps);
                    if (component == Component.ACCELEROMETER)
                        return databaseHandler.getAccelerometerMinEventsInInterval(begin, end, sampleInterval, fillGaps);
                    break;
                case MEDIAN:
                    if (component == Component.GYROSCOPE)
                        return databaseHandler.getGyroscopeMedianEventsInInterval(begin, end, sampleInterval, fillGaps);
                    if (component == Component.ACCELEROMETER)
                        return databaseHandler.getAccelerometerMedianEventsInInterval(begin, end, sampleInterval, fillGaps);
                    break;
                case LAST:
                    switch (component) {
                        case MOTOR:
                            return databaseHandler.getMotorControllerLastEventsInInterval(begin, end, sampleInterval, fillGaps);
                        case PROXIMITY:
                            return databaseHandler.getProximityLastEventsInInterval(begin, end, sampleInterval, fillGaps);
                        case GYROSCOPE:
                            return databaseHandler.getGyroscopeLastEventsInInterval(begin, end, sampleInterval, fillGaps);
                        case ACCELEROMETER:
                            return databaseHandler.getAccelerometerLastEventsInInterval(begin, end, sampleInterval, fillGaps);
                        default:
                            throw new LegoTruckException(Errors.INVALID_PARAMETER);
                    }
                default:
                    throw new LegoTruckException(Errors.INVALID_PARAMETER);

            }
        } catch (NullPointerException | IllegalArgumentException np) {
            throw new LegoTruckException(Errors.RESOURCE_EMPTY, comp, transform);
        }
        throw new LegoTruckException(Errors.INVALID_PARAMETER);
    }


}




