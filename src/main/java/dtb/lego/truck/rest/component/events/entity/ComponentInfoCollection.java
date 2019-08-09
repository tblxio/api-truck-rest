package dtb.lego.truck.rest.component.events.entity;

import dtb.lego.truck.rest.data.events.acquisition.entity.LegoTruckException;
import dtb.lego.truck.rest.errors.Errors;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * This class contain a collection with the information regarding the different
 * components registered in the system.
 */
@Getter
@Setter
@org.springframework.stereotype.Component
public class ComponentInfoCollection {
    CopyOnWriteArrayList<ComponentInfo> componentInfos;

    public ComponentInfoCollection() {
        this.componentInfos = new CopyOnWriteArrayList<>();
    }

    /**
     * Returns the information regarding a single component from the collection. This might not be the ideal way to
     * search through the CopyOnArrayList collection, but it works quite fast for this size of array.
     *
     * @param component The requested component.
     */
    public ComponentInfo getComponentInfo(Component component) {
        List<ComponentInfo> components = componentInfos.stream().filter(item -> item.getName().equals(component)).
                collect(Collectors.toList());
        if (components.isEmpty()) throw new LegoTruckException(Errors.RESOURCE_EMPTY, component, "disconnected");
        return components.get(0);
    }
}
