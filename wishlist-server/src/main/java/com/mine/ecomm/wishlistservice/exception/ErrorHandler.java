package com.mine.ecomm.wishlistservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException exception, WebRequest webRequest) {
        return new ResponseEntity<>("Fetching product details based on ID is failed", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        final StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            sb.append(String.format("%s", error.getDefaultMessage()));
            sb.append("\n");
        });

        return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
    }
}