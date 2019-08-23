package io.techhublisbon.lego.truck.rest;

import io.techhublisbon.lego.truck.rest.errors.Errors;
import io.techhublisbon.lego.truck.rest.events.acquisition.entity.LegoTruckException;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Arrays;

public class LegoTruckExceptionMatcher extends BaseMatcher<LegoTruckException> {
    private final Errors legoTruckError;
    private final Object[] parameters;

    public LegoTruckExceptionMatcher(final Errors legoTruckError, final Object... parameters) {
        this.legoTruckError = legoTruckError;
        this.parameters = parameters;
    }

    @Override
    public boolean matches(Object item) {
        if (!(item instanceof LegoTruckException)) {
            return false;
        }

        final LegoTruckException other = (LegoTruckException) item;

        return legoTruckError == other.getError() && Arrays.equals(parameters, other.getMessageParameters());
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(legoTruckError.toString() + " with parameters " + Arrays.toString(parameters));
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        if (!(item instanceof LegoTruckException)) {
            description.appendText("was ").appendValue(item);
        } else {
            final LegoTruckException other = (LegoTruckException) item;
            description.appendValue(other.getError().toString() + " with parameters " + Arrays.toString(other.getMessageParameters()));
        }
    }
}