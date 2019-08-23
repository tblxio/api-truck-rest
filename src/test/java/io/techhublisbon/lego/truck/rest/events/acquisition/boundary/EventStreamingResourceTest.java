package io.techhublisbon.lego.truck.rest.events.acquisition.boundary;

import io.techhublisbon.lego.truck.rest.LegoTruckExceptionMatcher;
import io.techhublisbon.lego.truck.rest.components.entity.Component;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfo;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfoCollection;
import io.techhublisbon.lego.truck.rest.components.entity.Transformation;
import io.techhublisbon.lego.truck.rest.errors.Errors;
import io.techhublisbon.lego.truck.rest.errors.InputValidator;
import io.techhublisbon.lego.truck.rest.events.acquisition.control.EventStreamingHandler;
import io.techhublisbon.lego.truck.rest.settings.control.WebSocketConfig;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EventStreamingResourceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private EventStreamingHandler mockEventStreamingHandler;
    @Mock
    private WebSocketConfig mockConfig;
    @Mock
    private ComponentInfoCollection componentInfoCollection;
    @InjectMocks
    private InputValidator mockInputValidator;

    private EventStreamingResource eventStreamingResourceUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        eventStreamingResourceUnderTest = new EventStreamingResource(mockEventStreamingHandler, mockConfig, mockInputValidator);
    }

    @Test
    public void testGetStreamPathSuccess() {
        // Setup
        final int interval = 1000;
        final String fields = "gyroscope";
        final String transformation = "mean";
        final ResponseEntity<String> expectedResult = ResponseEntity.ok().body(null);
        when(mockEventStreamingHandler.getStream(0, "fields", "transformation")).thenReturn(null);
        when(componentInfoCollection.getComponentInfo(any())).thenReturn(new ComponentInfo("gyroscope", 0.01));

        // Run the test
        final ResponseEntity<String> result = eventStreamingResourceUnderTest.getStreamPath(interval, fields, transformation);

        // Verify the results
        assertEquals(expectedResult, result);

    }

    @Test
    public void testGetStreamPathInvalidComponent() {
        // Setup
        final int interval = 1000;
        final String fields = "test";
        final String transformation = "mean";
        when(mockEventStreamingHandler.getStream(0, "fields", "transformation")).thenReturn(null);
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, fields));
        // Run the test
        final ResponseEntity<String> result = eventStreamingResourceUnderTest.getStreamPath(interval, fields, transformation);
    }

    @Test
    public void testGetStreamPathInvalidTransformation() {
        // Setup
        final int interval = 1000;
        final String fields = "motor";
        final String transformation = "test";
        when(mockEventStreamingHandler.getStream(0, "fields", "transformation")).thenReturn(null);
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, transformation));
        // Run the test
        final ResponseEntity<String> result = eventStreamingResourceUnderTest.getStreamPath(interval, fields, transformation);
    }

    @Test
    public void testGetStreamPathInvalidComponentTransformationPair() {
        // Setup
        final int interval = 1000;
        final String fields = "motor";
        final String transformation = "mean";
        final Transformation transformation1 = Transformation.MEAN;
        final Component component = Component.MOTOR;
        when(mockEventStreamingHandler.getStream(0, "fields", "transformation")).thenReturn(null);
        thrown.expect(new LegoTruckExceptionMatcher(Errors.RESOURCE_NOT_FOUND, component, transformation1));
        // Run the test
        final ResponseEntity<String> result = eventStreamingResourceUnderTest.getStreamPath(interval, fields, transformation);
    }

    @Test
    public void testGetStreamPathInvalidSamplingInterval() {
        // Setup
        final int interval = 1;
        final String fields = "motor";
        final String transformation = "last";
        when(mockEventStreamingHandler.getStream(0, "fields", "transformation")).thenReturn(null);
        when(componentInfoCollection.getComponentInfo(any())).thenReturn(new ComponentInfo("gyroscope", 0.01));
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, "interval requested is smaller than acquisition interval"));
        // Run the test
        final ResponseEntity<String> result = eventStreamingResourceUnderTest.getStreamPath(interval, fields, transformation);
    }


    @Test
    public void testGetWebSocketEndpoint() {
        // Setup
        final ResponseEntity<String> expectedResult = ResponseEntity.ok().body("result");
        when(mockConfig.getEndpoint()).thenReturn("result");

        // Run the test
        final ResponseEntity<String> result = eventStreamingResourceUnderTest.getWebSocketEndpoint();

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
