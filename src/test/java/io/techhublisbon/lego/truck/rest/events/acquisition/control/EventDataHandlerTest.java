package io.techhublisbon.lego.truck.rest.events.acquisition.control;

import io.techhublisbon.lego.truck.rest.LegoTruckExceptionMatcher;
import io.techhublisbon.lego.truck.rest.components.entity.Component;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfo;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfoCollection;
import io.techhublisbon.lego.truck.rest.components.entity.Transformation;
import io.techhublisbon.lego.truck.rest.errors.Errors;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class EventDataHandlerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private DatabaseHandler databaseHandler;
    @Mock
    private ComponentInfoCollection componentInfoCollection;
    @InjectMocks
    private EventDataHandler classUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void getComponentInfo() {
        //given
        ComponentInfo testComponent = new ComponentInfo("motor", 1.0);
        //when
        Mockito.when(componentInfoCollection.getComponentInfo(any(Component.class))).thenReturn(testComponent);
        //then
        assertEquals(testComponent, componentInfoCollection.getComponentInfo(Component.MOTOR));
    }

    @Test
    public void getMaxXYZEventInInterval() {
        //given
        final String componentString = "gyroscope";
        final Component component = Component.GYROSCOPE;
        final long interval = 1000;
        final String transformation = "max";
        final long timestamp = System.currentTimeMillis();
        final double x = 1.0f;
        final double y = 1.0f;
        final double z = 1.0f;
        XYZSensorEvent expectedResponse = new XYZSensorEvent(timestamp, x, y, z, componentString);
        given(databaseHandler.getXYZSensorMaxEventInLastInterval(interval, component)).willReturn(new XYZSensorEvent(timestamp, x, y, z, componentString));
        //when
        Event response = classUnderTest.getTransformedEvent(interval, componentString, transformation);
        //then
        assertEquals(response, expectedResponse);
    }

    @Test
    public void getMinXYZEventInIntervalTest() {
        //given
        final String componentString = "gyroscope";
        final Component component = Component.GYROSCOPE;
        final long interval = 1000;
        final String transformation = "min";
        final long timestamp = System.currentTimeMillis();
        final double x = 1.0f;
        final double y = 1.0f;
        final double z = 1.0f;
        XYZSensorEvent expectedResponse = new XYZSensorEvent(timestamp, x, y, z, componentString);
        given(databaseHandler.getXYZSensorMinEventInLastInterval(interval, component)).willReturn(new XYZSensorEvent(timestamp, x, y, z, componentString));
        //when
        Event response = classUnderTest.getTransformedEvent(interval, componentString, transformation);
        //then
        assertEquals(response, expectedResponse);
    }

    @Test
    public void getMeanXYZEventInIntervalTest() {
        //given
        final String componentString = "gyroscope";
        final Component component = Component.GYROSCOPE;
        final long interval = 1000;
        final String transformation = "mean";
        final long timestamp = System.currentTimeMillis();
        final double x = 1.0f;
        final double y = 1.0f;
        final double z = 1.0f;
        XYZSensorEvent expectedResponse = new XYZSensorEvent(timestamp, x, y, z, componentString);
        given(databaseHandler.getXYZSensorMeanEventInLastInterval(interval, component)).willReturn(new XYZSensorEvent(timestamp, x, y, z, componentString));
        //when
        Event response = classUnderTest.getTransformedEvent(interval, componentString, transformation);
        //then
        assertEquals(response, expectedResponse);
    }

    @Test
    public void getLastXYZEventInIntervalTest() {
        //given
        final String componentString = "gyroscope";
        final Component component = Component.GYROSCOPE;
        final long interval = 1000;
        final String transformation = "last";
        final long timestamp = System.currentTimeMillis();
        final double x = 1.0f;
        final double y = 1.0f;
        final double z = 1.0f;
        XYZSensorEvent expectedResponse = new XYZSensorEvent(timestamp, x, y, z, componentString);
        given(databaseHandler.getLastXYZSensorEvent(component)).willReturn(new XYZSensorEvent(timestamp, x, y, z, componentString));
        //when
        Event response = classUnderTest.getTransformedEvent(interval, componentString, transformation);
        //then
        assertEquals(response, expectedResponse);
    }

    @Test
    public void getLastMotorEventInIntervalTest() {
        //given
        final String componentString = "motor";
        final Component component = Component.MOTOR;
        final long interval = 1000;
        final String transformation = "last";
        final long timestamp = System.currentTimeMillis();
        final double voltage = 1.0f;
        final double temperature = 1.0f;
        MotorControllerEvent expectedResponse = new MotorControllerEvent(timestamp, voltage, temperature, componentString);
        given(databaseHandler.getLastMotorControllerEvent()).willReturn(new MotorControllerEvent(timestamp, voltage, temperature, componentString));
        //when
        Event response = classUnderTest.getTransformedEvent(interval, componentString, transformation);
        //then
        assertEquals(response, expectedResponse);
    }

    @Test
    public void getLastProximityEventInIntervalTest() {
        //given
        final String componentString = "proximity";
        final Component component = Component.PROXIMITY;
        final long interval = 1000;
        final String transformation = "last";
        final long timestamp = System.currentTimeMillis();
        final double distance = 1.0f;
        ProximitySensorEvent expectedResponse = new ProximitySensorEvent(timestamp, distance, componentString);
        given(databaseHandler.getLastProximityEvent()).willReturn(new ProximitySensorEvent(timestamp, distance, componentString));
        //when
        Event response = classUnderTest.getTransformedEvent(interval, componentString, transformation);
        //then
        assertEquals(response, expectedResponse);
    }

    @Test
    public void getEventInIntervalInvalidTransformationTest() {
        //given
        final String componentString = "proximity";

        final long interval = 1000;
        final String transformationString = "mean";
        final Transformation transformation = Transformation.MEAN;
        //then
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, transformationString));
        //when
        Event response = classUnderTest.getTransformedEvent(interval, componentString, transformationString);
    }

    @Test
    public void getEventInIntervalNoDataTest() {
        //given
        final String componentString = "proximity";
        final long interval = 1000;
        final String transformationString = "last";
        final Transformation transformation = Transformation.LAST;
        given(databaseHandler.getLastProximityEvent()).willThrow(NullPointerException.class);
        //then
        thrown.expect(new LegoTruckExceptionMatcher(Errors.RESOURCE_EMPTY, componentString, transformationString));
        //when
        Event response = classUnderTest.getTransformedEvent(interval, componentString, transformationString);
    }


    @Test
    public void getLastXYZTransformedEventHistoryTest() {
        //given
        final String componentString = "accelerometer";
        final Component component = Component.ACCELEROMETER;
        final long interval = 1000;
        final String transformation = "last";
        final long begin = 1000;
        final long end = 1000;
        final long sampleInterval = 100;
        boolean fillGaps = false;
        Collection<AccelerometerEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "accelerometer"));
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "accelerometer"));
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "accelerometer"));
        given(databaseHandler.getAccelerometerLastEventsInInterval(begin, end, sampleInterval, fillGaps)).willReturn(expectedRespose);

        Collection<? extends Event> response = classUnderTest.getTransformedEventHistory(sampleInterval, componentString, transformation, begin, end, fillGaps);
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getMaxXYZTransformedEventHistoryTest() {
        //given
        final String componentString = "accelerometer";
        final Component component = Component.ACCELEROMETER;
        final long interval = 1000;
        final String transformation = "max";
        final long begin = 1000;
        final long end = 1000;
        final long sampleInterval = 100;
        boolean fillGaps = false;
        Collection<AccelerometerEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "accelerometer"));
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "accelerometer"));
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "accelerometer"));
        given(databaseHandler.getAccelerometerMaxEventsInInterval(begin, end, sampleInterval, fillGaps)).willReturn(expectedRespose);

        Collection<? extends Event> response = classUnderTest.getTransformedEventHistory(sampleInterval, componentString, transformation, begin, end, fillGaps);
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getMinXYZTransformedEventHistoryTest() {
        //given
        final String componentString = "accelerometer";
        final Component component = Component.ACCELEROMETER;
        final long interval = 1000;
        final String transformation = "min";
        final long begin = 1000;
        final long end = 1000;
        final long sampleInterval = 100;
        boolean fillGaps = false;
        Collection<AccelerometerEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "accelerometer"));
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "accelerometer"));
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "accelerometer"));
        given(databaseHandler.getAccelerometerMinEventsInInterval(begin, end, sampleInterval, fillGaps)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getTransformedEventHistory(sampleInterval, componentString, transformation, begin, end, fillGaps);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getMedianXYZTransformedEventHistoryTest() {
        //given
        final String componentString = "accelerometer";
        final Component component = Component.ACCELEROMETER;
        final long interval = 1000;
        final String transformation = "median";
        final long begin = 1000;
        final long end = 1000;
        final long sampleInterval = 100;
        boolean fillGaps = false;
        Collection<AccelerometerEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "accelerometer"));
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "accelerometer"));
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "accelerometer"));
        given(databaseHandler.getAccelerometerMedianEventsInInterval(begin, end, sampleInterval, fillGaps)).willReturn(expectedRespose);

        Collection<? extends Event> response = classUnderTest.getTransformedEventHistory(sampleInterval, componentString, transformation, begin, end, fillGaps);
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getTransformedEventHistoryNoData() {
        //given
        final String componentString = "accelerometer";
        final Component component = Component.ACCELEROMETER;
        final long interval = 1000;
        final String transformation = "last";
        final long begin = 1000;
        final long end = 1000;
        final long sampleInterval = 100;
        boolean fillGaps = false;
        given(databaseHandler.getAccelerometerLastEventsInInterval(begin, end, sampleInterval, fillGaps)).willThrow(NullPointerException.class);
        //then
        thrown.expect(new LegoTruckExceptionMatcher(Errors.RESOURCE_EMPTY, componentString, transformation));
        //when
        classUnderTest.getTransformedEventHistory(sampleInterval, componentString, transformation, begin, end, fillGaps);
    }

    @Test
    public void getTransformedEventHistoryInvalidComponent() {
        //given
        final String componentString = "motor";
        final Component component = Component.MOTOR;
        final long interval = 1000;
        final String transformation = "mean";
        final long begin = 1000;
        final long end = 1000;
        final long sampleInterval = 100;
        boolean fillGaps = false;
        given(databaseHandler.getAccelerometerMaxEventsInInterval(begin, end, sampleInterval, fillGaps)).willReturn(null);
        //then
        thrown.expect(new LegoTruckExceptionMatcher(Errors.RESOURCE_NOT_FOUND, component, "last"));
        //when
        classUnderTest.getTransformedEventHistory(sampleInterval, componentString, transformation, begin, end, fillGaps);
    }

    @Test
    public void getAccelerometerEventsInIntervalTest() {
        //given
        final String componentString = "accelerometer";
        final Component component = Component.ACCELEROMETER;
        final long begin = 1000;
        final long end = 1000;
        Collection<AccelerometerEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "accelerometer"));
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "accelerometer"));
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "accelerometer"));
        given(databaseHandler.getAccelerometerEventsInInterval(begin, end)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getEventsInInterval(componentString, begin, end);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getGyroscopeEventsInIntervalTest() {
        //given
        final String componentString = "gyroscope";
        final Component component = Component.GYROSCOPE;
        final long begin = 1000;
        final long end = 1000;
        Collection<GyroscopeEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "accelerometer"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "accelerometer"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "accelerometer"));
        given(databaseHandler.getGyroscopeEventsInInterval(begin, end)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getEventsInInterval(componentString, begin, end);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getMotorEventsInIntervalTest() {
        //given
        final String componentString = "motor";
        final Component component = Component.MOTOR;
        final long begin = 1000;
        final long end = 1000;
        Collection<MotorControllerEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new MotorControllerEvent(System.currentTimeMillis(), 1.2d, 1.3d, "motor"));
        expectedRespose.add(new MotorControllerEvent(System.currentTimeMillis(), 1.3d, 1.4d, "motor"));
        expectedRespose.add(new MotorControllerEvent(System.currentTimeMillis(), 1.4d, 1.5d, "motor"));
        given(databaseHandler.getMotorEventsInInterval(begin, end)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getEventsInInterval(componentString, begin, end);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getProximityEventsInIntervalTest() {
        //given
        final String componentString = "proximity";
        final Component component = Component.PROXIMITY;
        final long begin = 1000;
        final long end = 1000;
        Collection<ProximitySensorEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new ProximitySensorEvent(System.currentTimeMillis(), 1.2d, "proximity"));
        expectedRespose.add(new ProximitySensorEvent(System.currentTimeMillis(), 1.3d, "proximity"));
        expectedRespose.add(new ProximitySensorEvent(System.currentTimeMillis(), 1.4d, "proximity"));
        given(databaseHandler.getProximityEventsInInterval(begin, end)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getEventsInInterval(componentString, begin, end);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void deleteEventsInIntervalTest() {
        //given
        final String componentString = "proximity";
        final Component component = Component.PROXIMITY;
        final long begin = 1000;
        final long end = 1000;

        classUnderTest.deleteEventsInInterval(componentString, begin, end);
    }
}