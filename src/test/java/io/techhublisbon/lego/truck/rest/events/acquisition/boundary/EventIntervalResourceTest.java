package io.techhublisbon.lego.truck.rest.events.acquisition.boundary;

import io.techhublisbon.lego.truck.rest.LegoTruckExceptionMatcher;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfo;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfoCollection;
import io.techhublisbon.lego.truck.rest.errors.Errors;
import io.techhublisbon.lego.truck.rest.errors.InputValidator;
import io.techhublisbon.lego.truck.rest.events.acquisition.control.EventDataHandler;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.Event;
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

public class EventIntervalResourceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private EventDataHandler mockEventDataHandler;
    @Mock
    private ComponentInfoCollection componentInfoCollection;
    @InjectMocks
    private InputValidator mockInputValidator;

    private EventIntervalResource eventIntervalResourceUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        eventIntervalResourceUnderTest = new EventIntervalResource(mockEventDataHandler, mockInputValidator);
    }

    @Test
    public void testGetComponentLastEventSuccess() {
        // Setup
        final String component = "motor";
        final ResponseEntity<Event> expectedResult = ResponseEntity.ok().body(null);
        when(mockEventDataHandler.getTransformedEvent(0L, "componentString", "transformationString")).thenReturn(null);

        // Run the test
        final ResponseEntity<Event> result = eventIntervalResourceUnderTest.getComponentLastEvent(component);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetComponentLastEventSuccessInvalidComponent() {
        // Setup
        final String component = "test";
        final ResponseEntity<Event> expectedResult = ResponseEntity.ok().body(null);
        when(mockEventDataHandler.getTransformedEvent(0L, "componentString", "transformationString")).thenReturn(null);
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, component));

        // Run the test
        final ResponseEntity<Event> result = eventIntervalResourceUnderTest.getComponentLastEvent(component);
    }

    @Test
    public void testGetComponentEventInSamplingIntervalSuccess() {
        // Setup
        final String component = "motor";
        final long interval = 1000L;
        final String transformation = "last";
        final ResponseEntity<Event> expectedResult = ResponseEntity.ok().body(null);
        when(mockEventDataHandler.getTransformedEvent(0L, "componentString", "transformationString")).thenReturn(null);
        when(componentInfoCollection.getComponentInfo(any())).thenReturn(new ComponentInfo("motor", 0.01));
        // Run the test
        final ResponseEntity<Event> result = eventIntervalResourceUnderTest.getComponentEventInSamplingInterval(component, interval, transformation);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetComponentEventInSamplingIntervalIllegalComponent() {
        // Setup
        final String component = "test";
        final long interval = 1000L;
        final String transformation = "last";
        final ResponseEntity<Event> expectedResult = ResponseEntity.ok().body(null);
        when(mockEventDataHandler.getTransformedEvent(0L, "componentString", "transformationString")).thenReturn(null);
        when(componentInfoCollection.getComponentInfo(any())).thenReturn(new ComponentInfo("motor", 0.01));
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, component));
        // Run the test
        final ResponseEntity<Event> result = eventIntervalResourceUnderTest.getComponentEventInSamplingInterval(component, interval, transformation);

    }

    @Test
    public void testGetComponentEventInSamplingIntervalIllegalTransformation() {
        // Setup
        final String component = "motor";
        final long interval = 1000L;
        final String transformation = "test";
        final ResponseEntity<Event> expectedResult = ResponseEntity.ok().body(null);
        when(mockEventDataHandler.getTransformedEvent(0L, "componentString", "transformationString")).thenReturn(null);
        when(componentInfoCollection.getComponentInfo(any())).thenReturn(new ComponentInfo("motor", 0.01));
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, transformation));
        // Run the test
        final ResponseEntity<Event> result = eventIntervalResourceUnderTest.getComponentEventInSamplingInterval(component, interval, transformation);
    }

    @Test
    public void testGetComponentEventInSamplingIntervalIllegalSamplingInterval() {
        // Setup
        final String component = "motor";
        final long interval = 1L;
        final String transformation = "last";
        final ResponseEntity<Event> expectedResult = ResponseEntity.ok().body(null);
        when(mockEventDataHandler.getTransformedEvent(0L, "componentString", "transformationString")).thenReturn(null);
        when(componentInfoCollection.getComponentInfo(any())).thenReturn(new ComponentInfo("motor", 0.01));
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, "interval requested is smaller than acquisition interval"));
        // Run the test
        final ResponseEntity<Event> result = eventIntervalResourceUnderTest.getComponentEventInSamplingInterval(component, interval, transformation);
    }


}
