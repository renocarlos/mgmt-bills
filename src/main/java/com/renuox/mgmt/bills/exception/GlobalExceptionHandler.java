package com.renuox.mgmt.bills.exception;

import com.renuox.mgmt.bills.response.ErrorResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> manageResourceNotFound(@NotNull ResourceNotFoundException ex) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorResponse error = new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), LocalDateTime.now(), "Resource not found", ex.getMessage()) {
        };
        return new ResponseEntity<>(error, httpStatus);
    }

    @ExceptionHandler(ResourceNotSavedException.class)
    public ResponseEntity<ErrorResponse> manageResourceNotSaved(@NotNull ResourceNotSavedException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse error = new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), LocalDateTime.now(), "Resource not saved", ex.getMessage()) {
        };
        return new ResponseEntity<>(error, httpStatus);
    }
}
