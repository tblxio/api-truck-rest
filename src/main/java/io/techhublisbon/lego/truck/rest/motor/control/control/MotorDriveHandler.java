package io.techhublisbon.lego.truck.rest.motor.control.control;

import io.techhublisbon.lego.truck.rest.MqttHandler;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * Implements all the functions provided by the Application in
 * regards to the Motor Controller, in this case the SBrick
 */
@Component
@DependsOn("MqttHandler")
public class MotorDriveHandler {

    private static final String DRIVE_TOPIC = "sbrick/01/sp/drive";
    private static final String SBRICK_ID = "88:6B:0F:80:29:D1";
    private static final float EXECUTION_TIME = 15f;
    private static final Logger LOG = LoggerFactory.getLogger(MotorDriveHandler.class);
    private MqttHandler myMqttHandler;

    @Autowired
    public MotorDriveHandler(MqttHandler myMqttHandler) {
        this.myMqttHandler = myMqttHandler;
    }

    /**
     * Generates a JSON payload to be sent to the SBrick via MQTT in order
     * to drive the motors, according to the framework defined in the protocol
     * defined in:
     * https://github.com/TechhubLisbon/sinfo-rpi-truck#step-3-communicate-with-the-sbrick
     *
     * @param channel   The channel to be driven
     * @param direction The direction, either clockwise(0) or counter-clockwise(1)
     * @param power     The power to be given to the motor
     * @return JSONObject JSON payload
     */
    private JSONObject generateDrivePayload(String channel, String direction, int power) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("sbrick_id", SBRICK_ID);
            payload.put("channel", channel);
            payload.put("direction", direction);
            payload.put("power", String.format("%02x", power));
            payload.put("exec_time", EXECUTION_TIME);
            payload.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            LOG.error("Exception: ", e);
        }

        return payload;
    }

    /**
     * Receives the motion and power requested by the user, converts it into a payload and
     * publishes it to the driveTopic to be received by the SBrick to perform the requested
     * drive action
     *
     * @param motion The motion requested, either linear or angular
     * @param power  The power to be given to the motor
     */
    public void drive(String motion, int power) {
        String channel;
        String direction;
        power *= 2.55; // Convert from 0-100 to 0-255
        direction = (power >= 0) ? "00" : "01";
        channel = (motion.equals("linear")) ? "02" : "00";
        JSONObject payload = generateDrivePayload(channel, direction, Math.abs(power));

        myMqttHandler.publishJson(payload, DRIVE_TOPIC);
    }


}
