package com.mine.ecomm.apigatewayservice.exception;

public class AuthException extends RuntimeException{
    public AuthException(String msg) {
        super(msg);
    }
}
