package dtb.lego.truck.rest;


import dtb.lego.truck.rest.settings.control.MqttConfig;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * Provides a handler to perform all the operations regarding the MQTT protocol.
 * It extends the MqttClient class in order to provide functionality and implement
 * settings specific to our use case.
 */
@Component("MqttHandler")
public class MqttHandler extends MqttClient {

    /**
     * The class constructor that perform the connection to the Mqtt broker
     * using the parameters defined in the MqttConfig object. It also publishes the
     * IP of the application to a specific topic, in order to provide easy access to
     * its functionality during testing.
     *
     * @param configuration The settings parameters for the MQTT connection
     */
    public MqttHandler(MqttConfig configuration) throws MqttException {
        super(configuration.getMqttHost(), MqttClient.generateClientId(), new MemoryPersistence());
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(configuration.getMqttUser());
        connOpts.setPassword(configuration.getMqttPass().toCharArray());
        this.connect(connOpts);
        this.publishAppIp();
    }

    /**
     * Static function that provides a way to handle MqttExceptions in a clean way
     *
     * @param me The thrown MqttException to be handled
     */
    public static void handle_execp_gracefully(MqttException me) {
        System.out.println("reason " + me.getReasonCode());
        System.out.println("msg " + me.getMessage());
        System.out.println("loc " + me.getLocalizedMessage());
        System.out.println("cause " + me.getCause());
        System.out.println("excep " + me);
        me.printStackTrace();
    }

    /**
     * Converts the received JSON into a Mqtt message and publishes it to the
     * given topic
     *
     * @param payload The JSON payload to be sent
     * @param topic   The topic to be published to
     */
    public void publishJson(JSONObject payload, String topic) {
        MqttMessage message = new MqttMessage(payload.toString().getBytes(StandardCharsets.UTF_8));
        try {
            this.publish(topic, message);
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            handle_execp_gracefully(e);
        }
    }

    /**
     * Gets the IP of the Application and publishes it to the predefined
     * joystick topic.
     * Used for testing purposes.
     */
    private void publishAppIp() throws MqttException {
        String ip = " ";
        try {
            ip = InetAddress.getLocalHost().getHostAddress() + ":8080/joystick";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        MqttMessage message = new MqttMessage(ip.getBytes());
        message.setRetained(true);
        this.publish("joystick", message);
    }

}
