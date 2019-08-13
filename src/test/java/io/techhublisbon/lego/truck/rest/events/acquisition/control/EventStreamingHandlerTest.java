package io.techhublisbon.lego.truck.rest.events.acquisition.control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.core.MessageSendingOperations;

import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

class EventStreamingHandlerTest {

    @Mock
    private MessageSendingOperations<String> messagingTemplate;

    @Mock
    private EventDataHandler eventDataHandler;

    @Mock
    private ConcurrentMap<String, String> subscriptions;

    @InjectMocks
    private EventStreamingHandler classUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
        //Test all the websocket functionality
    void streamerTest() {
        //given

        //then

        //when
    }

    @Test
    void getStreamTest() {
        //given
        String sessionId = "1234";
        String destination = "1234";
        String fields = "accelerometer-gyroscope";
        String transformation = "1234";
        int interval = 1234;
        String expected = "/events/" + interval + "/" + fields + "/" + transformation;
        //when
        String result = classUnderTest.getStream(interval, fields, transformation);
        String[] resultSubstring = result.split("/");
        String resultStringWithoutRandom = "/" + resultSubstring[1] + "/" + resultSubstring[2] + "/" + resultSubstring[3] + "/" + resultSubstring[4];
        //then
        assertEquals(resultStringWithoutRandom, expected);

    }
}