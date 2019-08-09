package io.techhublisbon.lego.truck.rest.events.acquisition.entity;

import io.techhublisbon.lego.truck.rest.errors.Errors;

import java.text.MessageFormat;

public class LegoTruckException extends RuntimeException {

    private final Errors myError;
    private final transient Object[] messageParameters;

    public LegoTruckException(final Errors error, final Object... messageParameters) {
        super(getErrorMessage(error, messageParameters));
        this.myError = error;
        this.messageParameters = messageParameters.clone();
    }

    public LegoTruckException(final Throwable cause, final Errors edfError, final Object... messageParameters) {
        super(getErrorMessage(edfError, messageParameters), cause);
        this.myError = edfError;
        this.messageParameters = messageParameters.clone();
    }

    private static String getErrorMessage(final Errors error, final Object[] messageParameters) {
        return MessageFormat.format(error.getErrorMessageTemplate(), messageParameters);
    }

    public Errors getError() {
        return myError;
    }

    public Object[] getMessageParameters() {
        return messageParameters;
    }
}
