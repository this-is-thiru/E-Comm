package com.mine.ecomm.sellerservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(MetadataException.class)
    public ResponseEntity<String> handleInvalidException(MetadataException exception, WebRequest webRequest) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleServiceException(ServiceException exception, WebRequest webRequest) {
        return new ResponseEntity<>("error", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException exception, WebRequest webRequest) {
        return new ResponseEntity<>("Product adding to all products catalog is failed", HttpStatus.NOT_MODIFIED);
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