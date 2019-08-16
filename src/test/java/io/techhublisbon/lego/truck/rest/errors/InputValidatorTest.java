package io.techhublisbon.lego.truck.rest.errors;

import io.techhublisbon.lego.truck.rest.LegoTruckExceptionMatcher;
import io.techhublisbon.lego.truck.rest.components.entity.Component;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfo;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfoCollection;
import io.techhublisbon.lego.truck.rest.components.entity.Transformation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;


public class InputValidatorTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private ComponentInfoCollection componentInfo;

    @InjectMocks
    private InputValidator inputValidator;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void checkValidComponentFail() {
        String componentString = "fake";
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, componentString));
        inputValidator.checkValidComponent(componentString);
    }

    @Test
    public void checkValidTransformationFail() {
        String transformationString = "fake";
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, transformationString));
        inputValidator.checkValidTransformation(transformationString);
    }

    @Test
    public void checkThatBeginIsBeforeEndFail() {
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, "'begin' must be before 'end'"));
        inputValidator.checkThatBeginIsBeforeEnd(System.currentTimeMillis(), System.currentTimeMillis() - 1);
    }

    @Test
    public void checkThatIntervalIsBiggerThanStorageIntervalFail() {
        given(componentInfo.getComponentInfo(any(Component.class))).willReturn(new ComponentInfo("motor", 1.0));
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, "interval requested is smaller than acquisition interval"));
        inputValidator.checkThatIntervalIsBiggerThanStorageInterval("motor", 10);
    }

    @Test
    public void checkValidStreamingComponentsFail() {
        String componentString = "motor";
        String transformationString = "mean";
        Component component = Component.MOTOR;
        Transformation transformation = Transformation.MEAN;
        thrown.expect(new LegoTruckExceptionMatcher(Errors.RESOURCE_NOT_FOUND, component, transformation));
        inputValidator.checkValidStreamingComponents(componentString, transformationString);
    }

    @Test
    public void checkValidComponentTransformationPairFail() {
        String componentString = "motor";
        String transformationString = "mean";
        Component component = Component.MOTOR;
        Transformation transformation = Transformation.MEAN;
        thrown.expect(new LegoTruckExceptionMatcher(Errors.RESOURCE_NOT_FOUND, component, transformation));
        inputValidator.checkValidComponentTransformationPair(componentString, transformationString);
    }

    @Test
    public void checkValidTransformationSuccess() {
        inputValidator.checkValidTransformation("mean");
    }

    @Test
    public void checkValidComponentSuccess() {
        inputValidator.checkValidComponent("motor");
    }

    @Test
    public void checkThatBeginIsBeforeEndSuccess() {
        inputValidator.checkThatBeginIsBeforeEnd(System.currentTimeMillis() - 1, System.currentTimeMillis());
    }

    @Test
    public void checkThatIntervalIsBiggerThanStorageIntervalSuccess() {
        given(componentInfo.getComponentInfo(any(Component.class))).willReturn(new ComponentInfo("motor", 1.0));
        inputValidator.checkThatIntervalIsBiggerThanStorageInterval("motor", 2000);
    }

    @Test
    public void checkValidComponentTransformationPairSuccess() {
        inputValidator.checkValidComponentTransformationPair("motor", "last");
    }

    @Test
    public void checkValidStreamingComponentsSuccess() {
        inputValidator.checkValidStreamingComponents("motor", "last");
    }

    @Test
    public void checkValidMotorMotionSuccessSuccess() {
        inputValidator.checkValidMotorMotion("linear");
    }

    @Test
    public void checkValidMotorMotionInvalidMotion() {
        String motionString = "fake";
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, "motion has to be either linear or angular"));
        inputValidator.checkValidMotorMotion(motionString);
    }

    @Test
    public void checkValidMotorPowerSuccessSuccess() {
        inputValidator.checkValidMotorPower(5);
    }

    @Test
    public void checkValidMotorPowerInvalidPower() {
        int invalidPower = 1000;
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, "power has to be smaller than 100"));
        inputValidator.checkValidMotorPower(invalidPower);
    }

}