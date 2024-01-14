package com.mine.ecomm.accountservice.exception;//package com.mine.ekart.users.exception;
//
//import com.mine.ekart.users.dto.CreateAccountRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//@RestControllerAdvice
//public class ErrorHandler extends ResponseEntityExceptionHandler {
//    @ExceptionHandler(InvalidException.class)
//    public ResponseEntity<String> handleInvalidException(InvalidException exception, WebRequest webRequest) {
//        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
//    }
//
//    @ExceptionHandler(ServiceException.class)
//    public ResponseEntity<CreateAccountRequest> handleServiceException(ServiceException exception, WebRequest webRequest) {
//        CreateAccountRequest n = new CreateAccountRequest();
//        n.setErrorMessage("error");
//        return new ResponseEntity<>(n, HttpStatus.NOT_ACCEPTABLE);
//    }
//}
