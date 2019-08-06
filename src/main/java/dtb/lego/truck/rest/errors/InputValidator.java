package dtb.lego.truck.rest.errors;

import dtb.lego.truck.rest.component.events.entity.Components;
import dtb.lego.truck.rest.component.events.entity.Transformations;
import dtb.lego.truck.rest.data.events.acquisition.entity.LegoTruckException;
import org.springframework.stereotype.Controller;

@Controller
public class InputValidator {
    public void checkValidComponent(String requestedComponent) {
        try {
            Components.valueOf(requestedComponent.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new LegoTruckException(Errors.INVALID_PARAMETER, requestedComponent);
        }
    }

    public void checkValidTransformation(String requestedTransformation) {
        try {
            Transformations.valueOf(requestedTransformation.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new LegoTruckException(Errors.INVALID_PARAMETER, requestedTransformation);
        }
    }

    public void checkThatBeginIsBeforeEnd(final long begin, final long end) {
        if (begin >= end) {
            throw new LegoTruckException(Errors.INVALID_PARAMETER, "'begin' must be before 'end'");
        }
    }

    public void checkThatIntervalIsBiggerThanStorageInterval(long interval) {
        if (interval <= 200) {
            throw new LegoTruckException(Errors.INVALID_PARAMETER, "interval requested is smaller than acquisition interval");
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
        Transformations transform = Transformations.valueOf(transformation.toUpperCase());
        Components component = Components.valueOf(requestedComponent.toUpperCase());
        if (component == Components.MOTOR && transform != Transformations.LAST)
            throw new LegoTruckException(Errors.RESOURCE_NOT_FOUND, requestedComponent, transformation);
    }

    public void checkBeginTransformedHistoryLessThan1MonthAgo(long begin) {
        long oneMonthAgo = (System.currentTimeMillis() - 2678400000L) * 1000;
        if (begin < oneMonthAgo)
            throw new LegoTruckException(Errors.INVALID_PARAMETER, "From needs to be later than two months ago");
    }
}
