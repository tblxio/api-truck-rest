package io.techhublisbon.lego.truck.rest.components.boundary;

import io.techhublisbon.lego.truck.rest.LegoTruckExceptionMatcher;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfo;
import io.techhublisbon.lego.truck.rest.errors.Errors;
import io.techhublisbon.lego.truck.rest.errors.InputValidator;
import io.techhublisbon.lego.truck.rest.events.acquisition.control.EventDataHandler;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ComponentsResourceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Spy
    private InputValidator mockInputValidator;
    @Mock
    private EventDataHandler mockEventDataHandler;

    private ComponentsResource componentsResourceUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        componentsResourceUnderTest = new ComponentsResource(mockInputValidator, mockEventDataHandler);
    }

    @Test
    public void testGetComponentInfo() {
        // Setup
        final String component = "motor";
        final ResponseEntity<ComponentInfo> expectedResult = ResponseEntity.ok().body(null);
        when(mockEventDataHandler.getComponentInfo(null)).thenReturn(null);

        // Run the test
        final ResponseEntity<ComponentInfo> result = componentsResourceUnderTest.getComponentInfo(component);

        // Verify the results
        assertEquals(expectedResult, result);

    }

    @Test
    public void testGetComponentInfoInvalidComponent() {
        // Setup
        final String component = "test";
        final ResponseEntity<ComponentInfo> expectedResult = ResponseEntity.ok().body(null);
        when(mockEventDataHandler.getComponentInfo(null)).thenReturn(null);
        thrown.expect(new LegoTruckExceptionMatcher(Errors.INVALID_PARAMETER, component));
        // Run the test
        final ResponseEntity<ComponentInfo> result = componentsResourceUnderTest.getComponentInfo(component);

    }

}
