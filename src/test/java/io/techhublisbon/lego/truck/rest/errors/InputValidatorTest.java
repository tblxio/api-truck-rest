package io.techhublisbon.lego.truck.rest.errors;

import io.techhublisbon.lego.truck.rest.components.entity.Component;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfo;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfoCollection;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.LegoTruckException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class InputValidatorTest {

    @Mock
    private ComponentInfoCollection componentInfo;

    @InjectMocks
    private InputValidator inputValidator;


    @Test
    void checkValidComponentFail() {
        assertThrows(LegoTruckException.class, () -> {
            inputValidator.checkValidComponent("fake");
        });
    }

    @Test
    void checkValidTransformationFail() {
        assertThrows(LegoTruckException.class, () -> {
            inputValidator.checkValidTransformation("fake");
        });
    }

    @Test
    void checkThatBeginIsBeforeEndFail() {
        assertThrows(LegoTruckException.class, () -> {
            inputValidator.checkThatBeginIsBeforeEnd(System.currentTimeMillis(), System.currentTimeMillis() - 1);
        });
    }

    @Test
    void checkThatIntervalIsBiggerThanStorageIntervalFail() {
        Mockito.when(componentInfo.getComponentInfo(any(Component.class))).thenReturn(new ComponentInfo("motor", 1.0));
        assertThrows(LegoTruckException.class, () -> {
            inputValidator.checkThatIntervalIsBiggerThanStorageInterval("motor", 10);
        });
    }

    @Test
    void checkValidStreamingComponentsFail() {
        assertThrows(LegoTruckException.class, () -> {
            inputValidator.checkValidStreamingComponents("motor", "mean");
        });
    }

    @Test
    void checkValidComponentTransformationPairFail() {
        assertThrows(LegoTruckException.class, () -> {
            inputValidator.checkValidComponentTransformationPair("motor", "mean");
        });
    }

    @Test
    void checkValidTransformationTest() {
        inputValidator.checkValidTransformation("mean");
    }

    @Test
    void checkValidComponent() {
        inputValidator.checkValidComponent("motor");
    }

    @Test
    void checkThatBeginIsBeforeEnd() {
        inputValidator.checkThatBeginIsBeforeEnd(System.currentTimeMillis() - 1, System.currentTimeMillis());
    }

    @Test
    void checkThatIntervalIsBiggerThanStorageInterval() {
        Mockito.when(componentInfo.getComponentInfo(any(Component.class))).thenReturn(new ComponentInfo("motor", 1.0));
        inputValidator.checkThatIntervalIsBiggerThanStorageInterval("motor", 2000);
    }

    @Test
    void checkValidComponentTransformationPair() {
        inputValidator.checkValidComponentTransformationPair("motor", "last");
    }

    @Test
    void checkValidStreamingComponents() {
        inputValidator.checkValidStreamingComponents("motor", "last");
    }

    @Test
    void checkValidMotorMotionSuccess() {
        inputValidator.checkValidMotorMotion("linear");
    }

    @Test
    void checkValidMotorMotionInvalidMotion() {
        assertThrows(LegoTruckException.class, () -> {
            inputValidator.checkValidMotorMotion("fail");
        });
    }

    @Test
    void checkValidMotorPowerSuccess() {
        inputValidator.checkValidMotorPower(5);
    }

    @Test
    void checkValidMotorPowerInvalidPower() {
        assertThrows(LegoTruckException.class, () -> {
            inputValidator.checkValidMotorPower(1000);
        });
    }

}