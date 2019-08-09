package io.techhublisbon.lego.truck.rest.errors;

import io.techhublisbon.lego.truck.rest.events.acquisition.entity.LegoTruckException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = LegoTruckException.class)
    public ResponseEntity<ErrorResponse> handleEdfException(final LegoTruckException ex) {
        final Errors error = ex.getError();
        final ErrorResponse errorResponse = new ErrorResponse(error.getErrorCode(), ex.getMessage());
        return builResponse(error.getResponseStatusCode(), errorResponse);
    }

    private ResponseEntity<ErrorResponse> builResponse(final int statusCode, final ErrorResponse errorResponse) {
        return ResponseEntity.status(statusCode)
                .body(errorResponse);
    }
}
