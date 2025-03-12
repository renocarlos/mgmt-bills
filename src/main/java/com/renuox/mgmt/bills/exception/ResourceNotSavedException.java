package com.renuox.mgmt.bills.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceNotSavedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotSavedException(String message) {
        super(message);
    }

}