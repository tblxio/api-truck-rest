package io.techhublisbon.lego.truck.rest.components.boundary;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.techhublisbon.lego.truck.rest.components.entity.Component;
import io.techhublisbon.lego.truck.rest.components.entity.ComponentInfo;
import io.techhublisbon.lego.truck.rest.components.entity.Transformation;
import io.techhublisbon.lego.truck.rest.errors.InputValidator;
import io.techhublisbon.lego.truck.rest.events.acquisition.control.EventDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("Operations pertaining the acquisition of information about the components in the system")
public class ComponentsResource {

    private InputValidator inputValidator;
    private EventDataHandler eventDataHandler;

    @Autowired
    public ComponentsResource(InputValidator inputValidator, EventDataHandler eventDataHandler) {
        this.inputValidator = inputValidator;
        this.eventDataHandler = eventDataHandler;
    }

    @ApiOperation(value = "Returns the components registered in the system")
    @GetMapping("/components")
    public ResponseEntity<Component[]> getComponents() {
        Component[] components = Component.values();
        return ResponseEntity.ok().body(components);
    }

    @ApiOperation(value = "Returns the information of the requested components")
    @GetMapping("/components/{component}")
    public ResponseEntity<ComponentInfo> getComponentInfo(@PathVariable String component) {
        inputValidator.checkValidComponent(component);
        ComponentInfo info = eventDataHandler.getComponentInfo(Component.valueOf(component.toUpperCase()));
        return ResponseEntity.ok().body(info);
    }

    @ApiOperation(value = "Returns the transformations registered in the system, which can be used when downsampling the data")
    @GetMapping("/transformations")
    public ResponseEntity<Transformation[]> getTransformations() {
        Transformation[] transformations = Transformation.values();
        return ResponseEntity.ok().body(transformations);
    }
}
