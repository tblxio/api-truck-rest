package io.techhublisbon.lego.truck.rest.events.acquisition.control;

import io.techhublisbon.lego.truck.rest.LegoTruckExceptionMatcher;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfoCollection;
import io.techhublisbon.lego.truck.rest.errors.Errors;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class MqttDataCallbackTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private ComponentInfoCollection componentInfoCollection;

    @Mock
    private DatabaseHandler databaseHandler;

    @InjectMocks
    private MqttDataCallback mqttDataCallbackUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void messageArrivedIMU() {
        //given
        String topic = "test/imu";
        String testMessage = "{\"gyro\":{\"x\": 123.4,\"y\": 133.9, \"z\": 122.4},\"accel\":{\"x\": 123.4,\"y\": 133.9, \"z\": 122.4},\"timestamp\": 1565370271265899}";
        MqttMessage testMqttMessage = new MqttMessage(testMessage.getBytes());
        //when
        mqttDataCallbackUnderTest.messageArrived(topic, testMqttMessage);
        //then
        verify(databaseHandler).saveXYZSensorEvent(any(), any());
    }

    @Test
    public void messageArrivedIMUMalFormedMessage() {
        //given
        String topic = "test/imu";
        String testMessage = "{}";
        MqttMessage testMqttMessage = new MqttMessage(testMessage.getBytes());
        //then
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INTERNAL_SERVER_ERROR, "Malformed MQTT message"));
        //when
        mqttDataCallbackUnderTest.messageArrived(topic, testMqttMessage);
    }

    @Test
    public void messageArrivedMotor() {
        //given
        String topic = "test/motor";
        String testMessage = "{\"resp_msg\":{\"voltage\": 123.4,\"temperature\": 133.9}}";
        MqttMessage testMqttMessage = new MqttMessage(testMessage.getBytes());
        //when
        mqttDataCallbackUnderTest.messageArrived(topic, testMqttMessage);
        //then
        verify(databaseHandler).saveMotorControllerEvent(any());
    }

    @Test
    public void messageArrivedMotorMalFormedMessage() {
        //given
        String topic = "test/motor";
        String testMessage = "{}";
        MqttMessage testMqttMessage = new MqttMessage(testMessage.getBytes());
        //then
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INTERNAL_SERVER_ERROR, "Malformed MQTT message"));
        //when
        mqttDataCallbackUnderTest.messageArrived(topic, testMqttMessage);
    }


    @Test
    public void messageArrivedProximity() {
        //given
        String topic = "test/proximity";
        String testMessage = "{\"distance\": 92.72, \"timestamp\": 1565370271065310}";
        MqttMessage testMqttMessage = new MqttMessage(testMessage.getBytes());
        //when
        mqttDataCallbackUnderTest.messageArrived(topic, testMqttMessage);
        //then
        verify(databaseHandler).saveProximitySensorEvent(any());
    }

    @Test
    public void messageArrivedProximityMalFormedMessage() {
        //given
        String topic = "test/proximity";
        String testMessage = "{}";
        MqttMessage testMqttMessage = new MqttMessage(testMessage.getBytes());
        //then
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INTERNAL_SERVER_ERROR, "Malformed MQTT message"));
        //when
        mqttDataCallbackUnderTest.messageArrived(topic, testMqttMessage);
    }

    @Test
    public void messageArrivedComponents() {
        //given
        String topic = "test/components";
        String testMessage = "{\"name\": \"proximity\", \"timestamp\": 1565368910783240, \"loopRate\": 0.1, \"topic\": \"truck1/proximity\", \"cycles\": 4, \"pollRate\": 0.4}";
        MqttMessage testMqttMessage = new MqttMessage(testMessage.getBytes());
        given(componentInfoCollection.getComponentInfos()).willReturn(new CopyOnWriteArrayList<>());
        //when
        mqttDataCallbackUnderTest.messageArrived(topic, testMqttMessage);
        //then
        verify(componentInfoCollection).getComponentInfos();
    }


    @Test
    public void messageArrivedComponentsMalFormedMessage() {
        //given
        String topic = "test/components";
        String testMessage = "{}";
        MqttMessage testMqttMessage = new MqttMessage(testMessage.getBytes());
        //then
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INTERNAL_SERVER_ERROR, "Malformed MQTT message"));
        //when
        mqttDataCallbackUnderTest.messageArrived(topic, testMqttMessage);
    }

    @Test
    public void messageArrivedCamera() {
        //given
        String topic = "test/camera";
        String testMessage = "{\"stream_port\": \"8000\", \"stream_ip\": \"192.168.1.143\", \"timestamp\": 1565370270816.0}";
        MqttMessage testMqttMessage = new MqttMessage(testMessage.getBytes());
        //when
        mqttDataCallbackUnderTest.messageArrived(topic, testMqttMessage);
        //then
        verify(componentInfoCollection).setVideoStream(any());
    }

    @Test
    public void messageArrivedCameraMalFormedMessage() {
        //given
        String topic = "test/camera";
        String testMessage = "{}";
        MqttMessage testMqttMessage = new MqttMessage(testMessage.getBytes());
        //then
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INTERNAL_SERVER_ERROR, "Malformed MQTT message"));
        //when
        mqttDataCallbackUnderTest.messageArrived(topic, testMqttMessage);
    }

}