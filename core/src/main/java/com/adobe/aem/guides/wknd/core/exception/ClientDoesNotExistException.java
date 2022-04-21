package com.adobe.aem.guides.wknd.core.exception;

public class ClientDoesNotExistException extends Exception {

    private static final long serialVersionUID = 1L;

    public ClientDoesNotExistException(String message) {
        super(message);
    }
}
