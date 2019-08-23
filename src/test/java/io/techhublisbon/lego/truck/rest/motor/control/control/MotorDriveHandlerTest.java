package io.techhublisbon.lego.truck.rest.motor.control.control;

import io.techhublisbon.lego.truck.rest.MqttHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class MotorDriveHandlerTest {

    @Mock
    private MqttHandler mockMqttHandler;

    private MotorDriveHandler motorDriveHandlerUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        motorDriveHandlerUnderTest = new MotorDriveHandler(mockMqttHandler);
    }

    @Test
    public void testDrive() throws Exception {
        // Setup
        final String motion = "linear";
        final int power = 0;

        // Run the test
        motorDriveHandlerUnderTest.drive(motion, power);

        // Verify the results
        verify(mockMqttHandler).publishJson(any(), any());
    }


}
