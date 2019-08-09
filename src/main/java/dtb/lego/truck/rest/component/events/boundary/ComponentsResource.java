package dtb.lego.truck.rest.component.events.boundary;

import dtb.lego.truck.rest.component.events.entity.Component;
import dtb.lego.truck.rest.component.events.entity.ComponentInfo;
import dtb.lego.truck.rest.component.events.entity.Transformation;
import dtb.lego.truck.rest.data.events.acquisition.control.SensorDataHandler;
import dtb.lego.truck.rest.errors.InputValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "Operations pertaining the acquisition of information about the components in the system")
public class ComponentsResource {

    private InputValidator inputValidator;
    private SensorDataHandler sensorDataHandler;

    @Autowired
    public ComponentsResource(InputValidator inputValidator, SensorDataHandler sensorDataHandler) {
        this.inputValidator = inputValidator;
        this.sensorDataHandler = sensorDataHandler;
    }

    @ApiOperation(value = "Returns the components registered in the system")
    @GetMapping("/components")
    public ResponseEntity<Component[]> getComponents() {
        Component[] components = Component.values();
        return ResponseEntity.ok()
                .body(components);
    }

    @ApiOperation(value = "Returns the information of the requested component")
    @GetMapping("/components/{component}")
    public ResponseEntity<ComponentInfo> getComponentInfo(@PathVariable String component) {
        inputValidator.checkValidComponent(component);
        ComponentInfo info = sensorDataHandler.getComponentInfo(Component.valueOf(component.toUpperCase()));
        return ResponseEntity.ok()
                .body(info);
    }

    @ApiOperation(value = "Returns the transformations registered in the system, which can be used when downsampling the data")
    @GetMapping("/transformations")
    public ResponseEntity<Transformation[]> getTransformations() {
        Transformation[] transformations = Transformation.values();
        return ResponseEntity.ok()
                .body(transformations);
    }
}
