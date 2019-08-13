package io.techhublisbon.lego.truck.rest.errors;

public enum Errors {
    MISSING_PARAMETER("1000", "Required parameter ''{0}'' is not present", 400), INVALID_PARAMETER("1001", "Invalid request parameter: {0}", 400), RESOURCE_NOT_FOUND("2001", "Requested resource not found ''{0}''::''{1}''.", 404), RESOURCE_EMPTY("2001", "No data found for ''{0}''::''{1}''", 404), INTERNAL_SERVER_ERROR("3000", "Internal server error.", 500);

    private final String errorCode;
    private final String errorMessageTemplate;
    private final int responseStatusCode;

    Errors(final String errorCode, final String errorMessageTemplate, final int responseStatusCode) {
        this.errorCode = errorCode;
        this.errorMessageTemplate = errorMessageTemplate;
        this.responseStatusCode = responseStatusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessageTemplate() {
        return errorMessageTemplate;
    }

    public int getResponseStatusCode() {
        return responseStatusCode;
    }

    @Override
    public String toString() {
        return "Error{" + "errorCode='" + errorCode + '\'' + ", errorMessageTemplate='" + errorMessageTemplate + '\'' + ", responseStatusCode=" + responseStatusCode + '}';
    }

}
