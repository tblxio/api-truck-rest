package io.techhublisbon.lego.truck.rest.events.acquisition.boundary;

import io.techhublisbon.lego.truck.rest.LegoTruckExceptionMatcher;
import io.techhublisbon.lego.truck.rest.errors.Errors;
import io.techhublisbon.lego.truck.rest.errors.InputValidator;
import io.techhublisbon.lego.truck.rest.events.acquisition.control.EventDataHandler;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdministratorResourceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Spy
    private InputValidator inputValidator;

    @Mock
    private EventDataHandler eventDataHandler;

    @InjectMocks
    private AdministratorResource administratorResourceUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void deleteEventHistoryFromComponentSuccess() {
        //given
        final String component = "motor";
        final ZonedDateTime from = ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("Z"));
        final ZonedDateTime to = ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 1), ZoneId.of("Z"));
        final ResponseEntity<String> expectedResult = ResponseEntity.ok().body("Deleted");
        //when
        final ResponseEntity<String> result = administratorResourceUnderTest.deleteEventHistoryFromComponent(component, from, to);
        //then
        assertEquals(expectedResult, result);
    }

    @Test
    public void deleteEventHistoryFromComponentInvalidComponent() {
        //given
        final String component = "test";
        final ZonedDateTime from = ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("Z"));
        final ZonedDateTime to = ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 1), ZoneId.of("Z"));
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, component));
        //when
        administratorResourceUnderTest.deleteEventHistoryFromComponent(component, from, to);
    }

    @Test
    public void deleteEventHistoryFromComponentInvalidFromTo() {
        //given
        final String component = "motor";
        final ZonedDateTime from = ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 1), ZoneId.of("Z"));
        final ZonedDateTime to = ZonedDateTime.of(LocalDateTime.of(2017, 1, 1, 0, 0, 0), ZoneId.of("Z"));
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, "'begin' must be before 'end'"));
        //when
        administratorResourceUnderTest.deleteEventHistoryFromComponent(component, from, to);
    }
}