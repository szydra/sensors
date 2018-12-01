package io.relayr.sensors.controller;

import io.relayr.sensors.exception.NoSuchSensorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchSensorException.class)
    protected ResponseEntity<Object> handleNoSuchSensorException(NoSuchSensorException exception,
                                                                 WebRequest request) {
        return handleExceptionInternal(exception, exception.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception,
                                                                        WebRequest request) {
        return handleExceptionInternal(exception, "Validation errors: " + exception.getMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
