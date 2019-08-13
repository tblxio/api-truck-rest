package io.techhublisbon.lego.truck.rest.motor.control.boundary;

import io.techhublisbon.lego.truck.rest.LegoTruckExceptionMatcher;
import io.techhublisbon.lego.truck.rest.errors.Errors;
import io.techhublisbon.lego.truck.rest.errors.InputValidator;
import io.techhublisbon.lego.truck.rest.motor.control.control.MotorDriveHandler;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

public class MotorResourceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Spy
    private InputValidator mockInputValidator;
    @Mock
    private MotorDriveHandler mockMotorDriveHandler;

    private MotorResource motorResourceUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        motorResourceUnderTest = new MotorResource(mockInputValidator, mockMotorDriveHandler);
    }

    @Test
    public void testDriveSuccess() {
        // Setup
        final String motion = "linear";
        final int power = 50;
        final ResponseEntity<String> expectedResult = ResponseEntity.ok().body("Drive command sent");

        // Run the test
        final ResponseEntity<String> result = motorResourceUnderTest.drive(motion, power);

        // Verify the results
        assertEquals(expectedResult, result);
    }


    @Test
    public void testDriveInvalidMotion() {
        // Setup
        final String motion = "motion";
        final int power = 50;
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, "motion has to be either linear or angular"));
        // Run the test
        final ResponseEntity<String> result = motorResourceUnderTest.drive(motion, power);

    }

    @Test
    public void testDriveInvalidPower() {
        // Setup
        final String motion = "linear";
        final int power = 1000;
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, "power has to be smaller than 100"));
        // Run the test
        final ResponseEntity<String> result = motorResourceUnderTest.drive(motion, power);

    }

}
