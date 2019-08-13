package io.techhublisbon.lego.truck.rest.events.acquisition.control;

import io.techhublisbon.lego.truck.rest.LegoTruckExceptionMatcher;
import io.techhublisbon.lego.truck.rest.components.control.AccelerometerEventRepository;
import io.techhublisbon.lego.truck.rest.components.control.GyroscopeEventRepository;
import io.techhublisbon.lego.truck.rest.components.control.MotorControllerEventRepository;
import io.techhublisbon.lego.truck.rest.components.control.ProximitySensorEventRepository;
import io.techhublisbon.lego.truck.rest.components.entity.Component;
import io.techhublisbon.lego.truck.rest.errors.Errors;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.AccelerometerEvent;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.Event;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.GyroscopeEvent;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.XYZSensorEvent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;


public class DatabaseHandlerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private AccelerometerEventRepository accelerometerEventRepository;
    @Mock
    private GyroscopeEventRepository gyroscopeEventRepository;
    @Mock
    private MotorControllerEventRepository motorControllerEventRepository;
    @Mock
    private ProximitySensorEventRepository proximitySensorEventRepository;


    private DatabaseHandler classUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        classUnderTest = new DatabaseHandler(accelerometerEventRepository, gyroscopeEventRepository, motorControllerEventRepository, proximitySensorEventRepository);
    }

    @Test
    public void getAccelerometerEventsInInterval() {
        //given
        long begin = 1000;
        long end = 1001;
        Collection<AccelerometerEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "accelerometer"));
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "accelerometer"));
        expectedRespose.add(new AccelerometerEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "accelerometer"));
        given(accelerometerEventRepository.findEventsInInterval(begin, end)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getAccelerometerEventsInInterval(begin, end);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getGyroscopeMeanEventsInInterval() {
        //given
        long begin = 1000;
        long end = 1001;
        long interval = 100;
        boolean fillGaps = false;
        Collection<GyroscopeEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "Gyroscope"));
        given(gyroscopeEventRepository.findAvgEventsInInterval(begin, end, interval)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getGyroscopeMeanEventsInInterval(begin, end, interval, fillGaps);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getGyroscopeMeanEventsInIntervalFillGaps() {
        //given
        long begin = 1000;
        long end = 1001;
        long interval = 100;
        boolean fillGaps = true;
        Collection<GyroscopeEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "Gyroscope"));
        given(gyroscopeEventRepository.findAvgEventsInIntervalFillingGaps(begin, end, interval)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getGyroscopeMeanEventsInInterval(begin, end, interval, fillGaps);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getGyroscopeMaxEventsInInterval() {
        //given
        long begin = 1000;
        long end = 1001;
        long interval = 100;
        boolean fillGaps = false;
        Collection<GyroscopeEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "Gyroscope"));
        given(gyroscopeEventRepository.findMaxEventsInInterval(begin, end, interval)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getGyroscopeMaxEventsInInterval(begin, end, interval, fillGaps);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getGyroscopeMaxEventsInIntervalFillGaps() {
        //given
        long begin = 1000;
        long end = 1001;
        long interval = 100;
        boolean fillGaps = true;
        Collection<GyroscopeEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "Gyroscope"));
        given(gyroscopeEventRepository.findMaxEventsInIntervalFillingGaps(begin, end, interval)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getGyroscopeMaxEventsInInterval(begin, end, interval, fillGaps);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getGyroscopeMinEventsInInterval() {
        //given
        long begin = 1000;
        long end = 1001;
        long interval = 100;
        boolean fillGaps = false;
        Collection<GyroscopeEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "Gyroscope"));
        given(gyroscopeEventRepository.findMinEventsInInterval(begin, end, interval)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getGyroscopeMinEventsInInterval(begin, end, interval, fillGaps);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getGyroscopeMinEventsInIntervalFillGaps() {
        //given
        long begin = 1000;
        long end = 1001;
        long interval = 100;
        boolean fillGaps = true;
        Collection<GyroscopeEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "Gyroscope"));
        given(gyroscopeEventRepository.findMinEventsInIntervalFillingGaps(begin, end, interval)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getGyroscopeMinEventsInInterval(begin, end, interval, fillGaps);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getGyroscopeLastEventsInInterval() {
        //given
        long begin = 1000;
        long end = 1001;
        long interval = 100;
        boolean fillGaps = false;
        Collection<GyroscopeEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "Gyroscope"));
        given(gyroscopeEventRepository.findLastEventsInInterval(begin, end, interval)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getGyroscopeLastEventsInInterval(begin, end, interval, fillGaps);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getGyroscopeLastEventsInIntervalFillGaps() {
        //given
        long begin = 1000;
        long end = 1001;
        long interval = 100;
        boolean fillGaps = true;
        Collection<GyroscopeEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "Gyroscope"));
        given(gyroscopeEventRepository.findLastEventsInIntervalFillingGaps(begin, end, interval)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getGyroscopeLastEventsInInterval(begin, end, interval, fillGaps);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getGyroscopeMedianEventsInInterval() {
        //given
        long begin = 1000;
        long end = 1001;
        long interval = 100;
        boolean fillGaps = false;
        Collection<GyroscopeEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "Gyroscope"));
        given(gyroscopeEventRepository.findMedianEventsInInterval(begin, end, interval)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getGyroscopeMedianEventsInInterval(begin, end, interval, fillGaps);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getGyroscopeMedianEventsInIntervalFillGaps() {
        //given
        long begin = 1000;
        long end = 1001;
        long interval = 100;
        boolean fillGaps = true;
        Collection<GyroscopeEvent> expectedRespose = new ArrayList<>();
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.3d, 1.4d, 1.2d, "Gyroscope"));
        expectedRespose.add(new GyroscopeEvent(System.currentTimeMillis(), 1.4d, 1.5d, 1.3d, "Gyroscope"));
        given(gyroscopeEventRepository.findMedianEventsInIntervalFillingGaps(begin, end, interval)).willReturn(expectedRespose);
        //when
        Collection<? extends Event> response = classUnderTest.getGyroscopeMedianEventsInInterval(begin, end, interval, fillGaps);
        //then
        assertEquals(response, expectedRespose);
    }

    @Test
    public void getXYZSensorMeanEventInLastInterval() {
        //given
        long interval = 1000;
        Component component = Component.GYROSCOPE;
        GyroscopeEvent expectedResponse = new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope");
        given(gyroscopeEventRepository.findEventMeaninLastTimeUnits(interval)).willReturn(expectedResponse);
        //when
        XYZSensorEvent response = classUnderTest.getXYZSensorMeanEventInLastInterval(interval, component);
        //then
        assertEquals(response, expectedResponse);
    }

    @Test
    public void getXYZSensorMeanEventInLastIntervalIllegalComponent() {
        //given
        long interval = 1000;
        Component component = Component.MOTOR;
        GyroscopeEvent expectedResponse = new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope");
        //then
        thrown.expect(new LegoTruckExceptionMatcher(Errors.RESOURCE_NOT_FOUND, component, "last"));
        //when
        XYZSensorEvent response = classUnderTest.getXYZSensorMeanEventInLastInterval(interval, component);

    }

    @Test
    public void getXYZSensorMaxEventInLastInterval() {
        //given
        long interval = 1000;
        Component component = Component.GYROSCOPE;
        GyroscopeEvent expectedResponse = new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope");
        given(gyroscopeEventRepository.findEventMaxinLastTimeUnits(interval)).willReturn(expectedResponse);
        //when
        XYZSensorEvent response = classUnderTest.getXYZSensorMaxEventInLastInterval(interval, component);
        //then
        assertEquals(response, expectedResponse);
    }

    @Test
    public void getXYZSensorMinEventInLastInterval() {
        //given
        long interval = 1000;
        Component component = Component.GYROSCOPE;
        GyroscopeEvent expectedResponse = new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope");
        given(gyroscopeEventRepository.findEventMininLastTimeUnits(interval)).willReturn(expectedResponse);
        //when
        XYZSensorEvent response = classUnderTest.getXYZSensorMinEventInLastInterval(interval, component);
        //then
        assertEquals(response, expectedResponse);
    }

    @Test
    public void getLastXYZSensorEvent() {
        //given
        long interval = 1000;
        Component component = Component.GYROSCOPE;
        GyroscopeEvent expectedResponse = new GyroscopeEvent(System.currentTimeMillis(), 1.2d, 1.3d, 1.4d, "Gyroscope");
        given(gyroscopeEventRepository.findLastEvent()).willReturn(expectedResponse);
        //when
        XYZSensorEvent response = classUnderTest.getLastXYZSensorEvent(component);
        //then
        assertEquals(response, expectedResponse);
    }

    @Test
    public void getLastMotorControllerEvent() {
        //given

        //then

        //when
    }

    @Test
    public void getLastProximityEvent() {
        //given

        //then

        //when
    }

    @Test
    public void deleteEventsInInterval() {
        //given

        //then

        //when
    }
}