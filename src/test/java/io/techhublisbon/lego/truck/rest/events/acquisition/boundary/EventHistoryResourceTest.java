package io.techhublisbon.lego.truck.rest.events.acquisition.boundary;

import io.techhublisbon.lego.truck.rest.LegoTruckExceptionMatcher;
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
import org.mockito.Spy;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EventHistoryResourceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private EventDataHandler mockEventDataHandler;
    @Spy
    private InputValidator mockInputValidator;
    @InjectMocks
    private EventHistoryResource eventHistoryResourceUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        //eventHistoryResourceUnderTest = new EventHistoryResource(mockEventDataHandler, mockInputValidator);
    }

    @Test
    public void testGetComponentEventHistoryInIntervalSuccess() {
        // Setup
        final String component = "motor";
        final ZonedDateTime from = ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("Z"));
        final ZonedDateTime to = ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 1), ZoneId.of("Z"));
        final ResponseEntity<Collection<? extends Event>> expectedResult = ResponseEntity.ok().body(Arrays.asList());
        when(mockEventDataHandler.getEventsInInterval("component", 0L, 0L)).thenReturn(Arrays.asList());

        // Run the test
        final ResponseEntity<Collection<? extends Event>> result = eventHistoryResourceUnderTest.getComponentEventHistoryInInterval(component, from, to);


        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetComponentEventHistoryInIntervalInvalidComponent() {
        // Setup
        final String component = "test";
        final ZonedDateTime from = ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("Z"));
        final ZonedDateTime to = ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 1), ZoneId.of("Z"));
        final ResponseEntity<Collection<? extends Event>> expectedResult = ResponseEntity.ok().body(Arrays.asList());
        when(mockEventDataHandler.getEventsInInterval("component", 0L, 0L)).thenReturn(Arrays.asList());

        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, component));
        // Run the test
        final ResponseEntity<Collection<? extends Event>> result = eventHistoryResourceUnderTest.getComponentEventHistoryInInterval(component, from, to);

    }

    @Test
    public void testGetComponentEventHistoryInIntervalInvalidFromTo() {
        // Setup
        final String component = "motor";
        final ZonedDateTime from = ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 1), ZoneId.of("Z"));
        final ZonedDateTime to = ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("Z"));
        final ResponseEntity<Collection<? extends Event>> expectedResult = ResponseEntity.ok().body(Arrays.asList());
        when(mockEventDataHandler.getEventsInInterval("component", 0L, 0L)).thenReturn(Arrays.asList());

        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, "'begin' must be before 'end'"));
        // Run the test
        final ResponseEntity<Collection<? extends Event>> result = eventHistoryResourceUnderTest.getComponentEventHistoryInInterval(component, from, to);
    }

    @Test
    public void testGetTransformedComponentEventHistoryInIntervalSuccess() {
        // Setup
        final String component = "motor";
        final ZonedDateTime from = ZonedDateTime.of(LocalDateTime.of(2019, 8, 1, 0, 0, 0), ZoneId.of("Z"));
        final ZonedDateTime to = ZonedDateTime.of(LocalDateTime.of(2019, 8, 1, 0, 0, 1), ZoneId.of("Z"));
        final int sampling = 0;
        final String transformation = "mean";
        final boolean fillGaps = false;
        final ResponseEntity<Collection<? extends Event>> expectedResult = ResponseEntity.ok().body(Arrays.asList());
        when(mockEventDataHandler.getTransformedEventHistory(0L, "comp", "transform", 0L, 0L, false)).thenReturn(Arrays.asList());

        // Run the test
        final ResponseEntity<Collection<? extends Event>> result = eventHistoryResourceUnderTest.getTransformedComponentEventHistoryInInterval(component, from, to, sampling, transformation, fillGaps);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetTransformedComponentEventHistoryInIntervalInvalidComponent() {
        // Setup
        final String component = "test";
        final ZonedDateTime from = ZonedDateTime.of(LocalDateTime.of(2019, 8, 1, 0, 0, 0), ZoneId.of("Z"));
        final ZonedDateTime to = ZonedDateTime.of(LocalDateTime.of(2019, 8, 1, 0, 0, 1), ZoneId.of("Z"));
        final int sampling = 0;
        final String transformation = "mean";
        final boolean fillGaps = false;
        final ResponseEntity<Collection<? extends Event>> expectedResult = ResponseEntity.ok().body(Arrays.asList());
        when(mockEventDataHandler.getTransformedEventHistory(0L, "comp", "transform", 0L, 0L, false)).thenReturn(Arrays.asList());
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, component));
        // Run the test
        final ResponseEntity<Collection<? extends Event>> result = eventHistoryResourceUnderTest.getTransformedComponentEventHistoryInInterval(component, from, to, sampling, transformation, fillGaps);
    }

    @Test
    public void testGetTransformedComponentEventHistoryInIntervalInvalidFromTo() {
        // Setup
        final String component = "motor";
        final ZonedDateTime from = ZonedDateTime.of(LocalDateTime.of(2019, 8, 1, 0, 0, 1), ZoneId.of("Z"));
        final ZonedDateTime to = ZonedDateTime.of(LocalDateTime.of(2019, 8, 1, 0, 0, 0), ZoneId.of("Z"));
        final int sampling = 0;
        final String transformation = "mean";
        final boolean fillGaps = false;
        final ResponseEntity<Collection<? extends Event>> expectedResult = ResponseEntity.ok().body(Arrays.asList());
        when(mockEventDataHandler.getTransformedEventHistory(0L, "comp", "transform", 0L, 0L, false)).thenReturn(Arrays.asList());
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, "'begin' must be before 'end'"));
        // Run the test
        final ResponseEntity<Collection<? extends Event>> result = eventHistoryResourceUnderTest.getTransformedComponentEventHistoryInInterval(component, from, to, sampling, transformation, fillGaps);
    }

    @Test
    public void testGetTransformedComponentEventHistoryInIntervalFromOlderThan1Month() {
        // Setup
        final String component = "motor";
        final ZonedDateTime from = ZonedDateTime.of(LocalDateTime.of(2018, 8, 1, 0, 0, 0), ZoneId.of("Z"));
        final ZonedDateTime to = ZonedDateTime.of(LocalDateTime.of(2019, 8, 1, 0, 0, 0), ZoneId.of("Z"));
        final int sampling = 0;
        final String transformation = "mean";
        final boolean fillGaps = false;
        final ResponseEntity<Collection<? extends Event>> expectedResult = ResponseEntity.ok().body(Arrays.asList());
        when(mockEventDataHandler.getTransformedEventHistory(0L, "comp", "transform", 0L, 0L, false)).thenReturn(Arrays.asList());
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, "From needs to be later than one month ago"));
        // Run the test
        final ResponseEntity<Collection<? extends Event>> result = eventHistoryResourceUnderTest.getTransformedComponentEventHistoryInInterval(component, from, to, sampling, transformation, fillGaps);
    }

    @Test
    public void testGetTransformedComponentEventHistoryInIntervalInvalidTransformation() {
        // Setup
        final String component = "motor";
        final ZonedDateTime from = ZonedDateTime.of(LocalDateTime.of(2019, 8, 1, 0, 0, 0), ZoneId.of("Z"));
        final ZonedDateTime to = ZonedDateTime.of(LocalDateTime.of(2019, 8, 1, 0, 0, 1), ZoneId.of("Z"));
        final int sampling = 0;
        final String transformation = "false";
        final boolean fillGaps = false;
        final ResponseEntity<Collection<? extends Event>> expectedResult = ResponseEntity.ok().body(Arrays.asList());
        when(mockEventDataHandler.getTransformedEventHistory(0L, "comp", "transform", 0L, 0L, false)).thenReturn(Arrays.asList());
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, transformation));
        // Run the test
        final ResponseEntity<Collection<? extends Event>> result = eventHistoryResourceUnderTest.getTransformedComponentEventHistoryInInterval(component, from, to, sampling, transformation, fillGaps);
    }


}
