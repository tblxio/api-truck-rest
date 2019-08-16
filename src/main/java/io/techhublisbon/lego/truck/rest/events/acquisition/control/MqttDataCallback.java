package io.techhublisbon.lego.truck.rest.events.acquisition.control;

import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfo;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfoCollection;
import io.techhublisbon.lego.truck.rest.components.entity.VideoStream;
import io.techhublisbon.lego.truck.rest.errors.Errors;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.*;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to implement the necessary operations for when a message arrives to the topics in which the
 * MqttClient is subscribed to, in this case the topics pertaining to the data from the sensors.
 */
public class MqttDataCallback implements MqttCallback {

    private static final Logger LOG = LoggerFactory.getLogger(MqttDataCallback.class);
    /**
     * This collections are used to store the data received by the sensors. They are initialized by the class instantiating
     * the callback handler. If there is a change to automatically store the messages as they come in there is no use
     * for this Collections.
     */
    private ComponentInfoCollection myComponents;
    private String mqttErrorString = "Malformed MQTT message";
    private DatabaseHandler databaseHandler;

    MqttDataCallback(ComponentInfoCollection myComponents, DatabaseHandler databaseHandler) {
        this.myComponents = myComponents;
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void connectionLost(Throwable cause) {
        LOG.debug("Connection lost: {0}", cause);
    }

    /**
     * Called asynchronously when a new message arrives, it dissects the message topic and then calls the correct function
     * to handle the data from that particular sender.
     *
     * @param topic   The topic from which the message arrived
     * @param message The message
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String[] subtopics = topic.split("/");
        String componentName = subtopics[1];
        if (componentName.equals("components")) this.handleComponentsMessage(message);
        if (componentName.equals("imu")) this.handleImuMessage(message);
        if (componentName.equals("motor")) this.handleMotorMessage(message);
        if (componentName.equals("proximity")) this.handleProximityMessage(message);
        if (componentName.equals("camera")) this.handleCameraMessage(message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        LOG.debug("Delivery complete");
    }


    /**
     * Handles the Component message, that contains information regarding the different components in the system,
     * creating the {@link ComponentInfo} object and adding it to the collection
     */
    private void handleComponentsMessage(MqttMessage message) {
        final String pollRate = "pollRate";
        try {
            JSONObject msg = new JSONObject(message.toString());
            // Save components and sample rate on the Collection
            if (msg.get("name").equals("imu")) {
                myComponents.getComponentInfos().add(new ComponentInfo("gyroscope", msg.get(pollRate)));
                myComponents.getComponentInfos().add(new ComponentInfo("accelerometer", msg.get(pollRate)));
            } else myComponents.getComponentInfos().add(new ComponentInfo(msg.get("name"), msg.get(pollRate)));
        } catch (Exception e) {
            LOG.debug("[COMPONENTS]: MQTT Data should be on the right format");
            throw new LegoTruckException(Errors.INTERNAL_SERVER_ERROR, mqttErrorString);
        }
    }

    /**
     * Handles the Imu message, that contains the measurements from the accelerometer and gyroscope,
     * creating the {@link AccelerometerEvent} and {@link GyroscopeEvent} object and adding it to the collection
     */
    private void handleImuMessage(MqttMessage message) {
        try {
            JSONObject msg = new JSONObject(message.toString());
            JSONObject accel = msg.getJSONObject("accel");
            JSONObject gyro = msg.getJSONObject("gyro");
            long timestamp = (long) msg.get("timestamp");
            GyroscopeEvent gyroscopeEvent = new GyroscopeEvent(timestamp / 1000, gyro.get("x"), gyro.get("y"), gyro.get("z"));
            AccelerometerEvent accelerometerEvent = new AccelerometerEvent(timestamp / 1000, accel.get("x"), accel.get("y"), accel.get("z"));

            databaseHandler.saveXYZSensorEvent(accelerometerEvent, gyroscopeEvent); // Inserts in local database takes around 1ms.
        } catch (Exception e) {
            LOG.debug("[IMU]: MQTT Data should be on the right format");
            throw new LegoTruckException(Errors.INTERNAL_SERVER_ERROR, mqttErrorString);
        }
    }

    /**
     * Handles the Motor message, that contains information regarding the state of the SBrick Controller,
     * creating the {@link MotorControllerEvent} object and adding it to the collection
     */
    private void handleMotorMessage(MqttMessage message) {
        try {
            JSONObject msg = new JSONObject(message.toString());
            JSONObject respMsg = msg.getJSONObject("resp_msg");
            MotorControllerEvent incoming = new MotorControllerEvent(System.currentTimeMillis(), respMsg.get("voltage"), respMsg.get("temperature"));
            databaseHandler.saveMotorControllerEvent(incoming);
        } catch (Exception e) {
            LOG.debug("[MOTOR]: MQTT Data should be on the right format");
            throw new LegoTruckException(Errors.INTERNAL_SERVER_ERROR, mqttErrorString);
        }
    }

    /**
     * Handles the Motor message, that contains information regarding the state of the SBrick Controller,
     * creating the {@link MotorControllerEvent} object and adding it to the collection
     */
    private void handleProximityMessage(MqttMessage message) {
        try {
            JSONObject msg = new JSONObject(message.toString());
            long timestamp = (long) msg.get("timestamp");
            databaseHandler.saveProximitySensorEvent(new ProximitySensorEvent(timestamp / 1000, msg.get("distance")));

        } catch (Exception e) {
            LOG.debug("[PROXIMITY]: MQTT Data should be on the right format");
            throw new LegoTruckException(Errors.INTERNAL_SERVER_ERROR, mqttErrorString);
        }
    }

    private void handleCameraMessage(MqttMessage message) {
        try {
            JSONObject msg = new JSONObject(message.toString());
            String ip = (String) msg.get("stream_ip");
            String port = (String) msg.get("stream_port");
            myComponents.setVideoStream(new VideoStream(ip, port));
        } catch (Exception e) {
            LOG.debug("[CAMERA]: MQTT Data should be on the right format");
            throw new LegoTruckException(Errors.INTERNAL_SERVER_ERROR, mqttErrorString);
        }
    }
}
