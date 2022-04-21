package com.adobe.aem.guides.wknd.core.exception;

public class JwtInvalidException extends Exception{
    public JwtInvalidException(String message) {
        super(message);
    }
}
