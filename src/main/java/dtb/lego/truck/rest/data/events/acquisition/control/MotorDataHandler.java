package dtb.lego.truck.rest.data.events.acquisition.control;

import dtb.lego.truck.rest.MqttHandler;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This class is used to request data from the Motor controller every X seconds, to be subsequently saved in the database
 * by the {@link SensorDataHandler} class/
 */
@Component
@Getter
public class MotorDataHandler {

    /**
     * The topic in which the SBrick is supposed to respond with the requested information
     */
    private final String adcResponseTopic = "truck1/motor";

    /**
     * The topic in which the SBrick is listenning for information requests
     */
    private final String adcRequestTopic = "sbrick/01/rr/get_adc";

    /**
     * MAC address of the SBRICK
     */
    private final String sbrickId = "88:6B:0F:80:29:D1";

    /**
     * Defines the interval between data acquisitions from the
     * Adc of the Motor Controller in milliseconds, namely the battery voltage
     * and the temperature. This interval is then multiplied by 10
     * to perform storage of the acquired values to the database
     */
    private final long dataAcquisitionInterval = 20000;

    @Autowired
    private MqttHandler myMqttHandler;

    /**
     * Generates a JSON payload to be sent to the SBrick via MQTT in order
     * to request the data from the controller, according to the framework defined
     * in the protocol defined in:
     * https://github.com/TechhubLisbon/sinfo-rpi-truck#step-3-communicate-with-the-sbrick
     *
     * @return JSONObject JSON payload
     */
    private JSONObject generateAdcInfoPayload() {
        JSONObject payload = new JSONObject();

        try {
            JSONObject req_msg = new JSONObject();
            req_msg.put("sbrick_id", sbrickId);

            payload.put("status", 0);
            payload.put("req_msg", req_msg.toString());
            payload.put("resp_topic", adcResponseTopic);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return payload;
    }

    /**
     * Requests the adcInfo from the SBrick at every dataAcquisitionInterval
     */
    @Scheduled(fixedRate = dataAcquisitionInterval)
    private void requestAdcInfo() {
        JSONObject requestPayload = generateAdcInfoPayload();
        myMqttHandler.publishJson(requestPayload, adcRequestTopic);
    }

}
