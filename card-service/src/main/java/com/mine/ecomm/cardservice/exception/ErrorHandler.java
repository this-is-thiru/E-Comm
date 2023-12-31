package com.mine.ecomm.cardservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


/**
 * The type Error handler.
 */
@RestControllerAdvice
public class ErrorHandler {
    /**
     * Handle metadata exception response entity.
     *
     * @param exception the exception
     * @param webRequest the web request
     *
     * @return the response entity
     */
    @ExceptionHandler(MetadataException.class)
    public ResponseEntity<String> handleMetadataException(MetadataException exception, WebRequest webRequest) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_MODIFIED);
    }


    /**
     * Handle method argument not valid response entity.
     *
     * @param ex the ex
     *
     * @return the response entity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        final StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            sb.append(String.format("%s", error.getDefaultMessage()));
            sb.append("\n");
        });

        return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValid1(MethodArgumentNotValidException ex) {
//        Map<String,String> errors=new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach((error) ->{
//            String fieldName=error.getField();
//            error.getDefaultMessage();
//            String message =error.getDefaultMessage();
//            errors.put(fieldName, message);
//        });
//
//        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
//
//    }
}