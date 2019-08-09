package dtb.lego.truck.rest.component.events.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents one component's information.
 * It contains it's name and the rate in which the data is acquired from the sensor.
 */
@Getter
@Setter
public class ComponentInfo {

    private Component name;
    private Double samplingInterval;

    public ComponentInfo(Object name, Object minSamplingInterval) {
        String component = (String) name;
        this.name = Component.valueOf(component.toUpperCase());
        this.samplingInterval = (Double) minSamplingInterval;
    }


}
