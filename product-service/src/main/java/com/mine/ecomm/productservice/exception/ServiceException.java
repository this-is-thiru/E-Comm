package com.mine.ecomm.productservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends Exception {
    private final HttpStatus status;
    public ServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
