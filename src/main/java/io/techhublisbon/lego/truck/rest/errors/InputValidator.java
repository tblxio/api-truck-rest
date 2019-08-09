package io.techhublisbon.lego.truck.rest.errors;

import io.techhublisbon.lego.truck.rest.component.entity.Component;
import io.techhublisbon.lego.truck.rest.component.entity.ComponentInfoCollection;
import io.techhublisbon.lego.truck.rest.component.entity.Transformation;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.LegoTruckException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class InputValidator {

    private ComponentInfoCollection componentInfoCollection;

    @Autowired
    public InputValidator(ComponentInfoCollection componentInfoCollection) {
        this.componentInfoCollection = componentInfoCollection;
    }

    public void checkValidComponent(String requestedComponent) {
        try {
            Component.valueOf(requestedComponent.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new LegoTruckException(Errors.INVALID_PARAMETER, requestedComponent);
        }
    }

    public void checkValidTransformation(String requestedTransformation) {
        try {
            Transformation.valueOf(requestedTransformation.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new LegoTruckException(Errors.INVALID_PARAMETER, requestedTransformation);
        }
    }

    public void checkThatBeginIsBeforeEnd(final long begin, final long end) {
        if (begin >= end) {
            throw new LegoTruckException(Errors.INVALID_PARAMETER, "'begin' must be before 'end'");
        }
    }

    public void checkThatIntervalIsBiggerThanStorageInterval(String fields, long interval) {
        String[] components = fields.split("-");
        for (String component : components) {
            double componentInterval = componentInfoCollection.getComponentInfo(Component.valueOf(component.toUpperCase())).getSamplingInterval();
            if (interval / 1000.0 < componentInterval) {
                throw new LegoTruckException(Errors.INVALID_PARAMETER, "interval requested is smaller than acquisition interval");
            }
        }
    }

    public void checkValidStreamingComponents(String requestedComponents, String transformation) {
        String[] components = requestedComponents.split("-");
        checkValidTransformation(transformation);
        for (String component : components) {
            checkValidComponent(component);
            checkValidComponentTransformationPair(component, transformation);
        }
    }

    public void checkValidComponentTransformationPair(String requestedComponent, String transformation) {
        Transformation transform = Transformation.valueOf(transformation.toUpperCase());
        Component component = Component.valueOf(requestedComponent.toUpperCase());
        if (component == Component.MOTOR || component == Component.PROXIMITY && transform != Transformation.LAST)
            throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, requestedComponent, transformation);
    }

    public void checkBeginTransformedHistoryLessThan1MonthAgo(long begin) {
        long oneMonthAgo = (System.currentTimeMillis() - 2678400000L);
        if (begin < oneMonthAgo)
            throw new LegoTruckException(Errors.INVALID_PARAMETER, "From needs to be later than two months ago");
    }
}
