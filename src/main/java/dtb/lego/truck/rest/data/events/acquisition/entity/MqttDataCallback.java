package dtb.lego.truck.rest.data.events.acquisition.entity;

import dtb.lego.truck.rest.component.events.entity.ComponentInfo;
import dtb.lego.truck.rest.component.events.entity.ComponentInfoCollection;
import dtb.lego.truck.rest.component.events.entity.events.MotorControllerEvent;
import dtb.lego.truck.rest.component.events.entity.events.ProximitySensorEvent;
import dtb.lego.truck.rest.component.events.entity.events.xyz.sensor.AccelerometerEvent;
import dtb.lego.truck.rest.component.events.entity.events.xyz.sensor.GyroscopeEvent;
import dtb.lego.truck.rest.data.events.acquisition.control.DatabaseHandler;
import dtb.lego.truck.rest.errors.Errors;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class is used to implement the necessary operations for when a message arrives to the topics in which the
 * MqttClient is subscribed to, in this case the topics pertaining to the data from the sensors.
 */
public class MqttDataCallback implements MqttCallback {

    /**
     * This collections are used to store the data received by the sensors. They are initialized by the class instantiating
     * the callback handler. If there is a change to automatically store the messages as they come in there is no use
     * for this Collections.
     */
    @Autowired
    private ComponentInfoCollection myComponents;

    private DatabaseHandler databaseHandler;

    public MqttDataCallback(ComponentInfoCollection myComponents,
                            DatabaseHandler databaseHandler) {
        this.myComponents = myComponents;
        this.databaseHandler = databaseHandler;
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause);
        cause.printStackTrace();
    }

    /**
     * Called asynchronously when a new message arrives, it dissects the message topic and then calls the correct function
     * to handle the data from that particular sender.
     *
     * @param topic   The topic from which the message arrived
     * @param message The message
     */
    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        String[] subtopics = topic.split("/");
        String componentName = subtopics[1];
        if (componentName.equals("components")) this.handleComponentsMessage(message);
        if (componentName.equals("imu")) this.handleImuMessage(message);
        if (componentName.equals("motor")) this.handleMotorMessage(message);
        if (componentName.equals("proximity")) this.handleProximityMessage(message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }


    /**
     * Handles the Components message, that contains information regarding the different components in the system,
     * creating the {@link ComponentInfo} object and adding it to the collection
     */
    private void handleComponentsMessage(MqttMessage message) {
        try {
            JSONObject msg = new JSONObject(message.toString());
            // Save component and sample rate on the Collection
            if (myComponents.getComponentInfos() == null) System.out.println("nill");
            if (msg.get("name").equals("imu")) {
                myComponents.getComponentInfos().add(new ComponentInfo("gyroscope", msg.get("pollRate")));
                myComponents.getComponentInfos().add(new ComponentInfo("accelerometer", msg.get("pollRate")));
            } else
                myComponents.getComponentInfos().add(new ComponentInfo(msg.get("name"), msg.get("pollRate")));
        } catch (Exception e) {
            // Catches exceptions related to malformed payloads
            e.printStackTrace();
            System.out.println("[Components] Data should be on the right format  + " + e.getCause());
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
            GyroscopeEvent gyroscopeEvent = new GyroscopeEvent(timestamp / 1000,
                    gyro.get("x"),
                    gyro.get("y"),
                    gyro.get("z"), "gyroscope");
            AccelerometerEvent accelerometerEvent = new AccelerometerEvent(timestamp / 1000,
                    accel.get("x"),
                    accel.get("y"),
                    accel.get("z"), "accelerometer");

            databaseHandler.saveXYZSensorEvent(accelerometerEvent, gyroscopeEvent); // Inserts in local database takes around 1ms.
        } catch (Exception e) {
            // Catches exceptions related to malformed payloads
            System.out.println("[Imu] Data should be on the right format  + " + e.getMessage());
            throw new LegoTruckException(Errors.INTERNAL_SERVER_ERROR, "Malformed MQTT message");
        }
    }

    /**
     * Handles the Motor message, that contains information regarding the state of the SBrick Controller,
     * creating the {@link MotorControllerEvent} object and adding it to the collection
     */
    private void handleMotorMessage(MqttMessage message) {
        try {
            JSONObject msg = new JSONObject(message.toString());
            JSONObject resp_msg = msg.getJSONObject("resp_msg");
            MotorControllerEvent incoming = new MotorControllerEvent(System.currentTimeMillis(), resp_msg.get("voltage"), resp_msg.get("temperature"), "motor");
            databaseHandler.saveMotorControllerEvent(incoming);
        } catch (Exception e) {
            System.out.println("Data should be on the right format  + " + e.getMessage());
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
            databaseHandler.saveProximitySensorEvent(new ProximitySensorEvent(timestamp / 1000, msg.get("distance"), "proximity"));

        } catch (Exception e) {
            System.out.println("[Proximity] Data should be on the right format  + " + e.getMessage());
        }
    }

}
