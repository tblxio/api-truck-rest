package io.techhublisbon.lego.truck.rest.errors;

import java.text.MessageFormat;
import java.util.Objects;

public class ErrorResponse {
    private final String errorCode;
    private final String errorMessage;

    public ErrorResponse(final String errorCode, final String messageTemplate, final Object... messageParameters) {
        this.errorCode = errorCode;
        this.errorMessage = messageParameters.length > 0 ? MessageFormat.format(messageTemplate, messageParameters) : messageTemplate;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorResponse)) return false;
        ErrorResponse that = (ErrorResponse) o;
        return Objects.equals(errorCode, that.errorCode) && Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode, errorMessage);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" + "errorCode='" + errorCode + '\'' + ", errorMessage='" + errorMessage + '\'' + '}';
    }
}
