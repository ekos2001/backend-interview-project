package com.ninjaone.backendinterviewproject.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@Log4j2
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<Object> handleDataIntegrity(CustomerAlreadyExistsException ex) {
        String message = "Customer already exists";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message));
    }

    @ExceptionHandler(DeviceAlreadyExistsException.class)
    public ResponseEntity<Object> handleDataIntegrity(DeviceAlreadyExistsException ex) {
        String message = "Device already exists";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message));
    }

    @ExceptionHandler(ServiceAlreadyExistsException.class)
    public ResponseEntity<Object> handleDataIntegrity(ServiceAlreadyExistsException ex) {
        String message = "Service already exists";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(EntityNotFoundException ex) {
        String message = ex.getLocalizedMessage();
        log.error(message);
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, message));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
