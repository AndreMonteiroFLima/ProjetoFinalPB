package com.adobe.aem.guides.wknd.core.exception;

public class CategoryDoesNotExistException extends Exception {

    private static final long serialVersionUID = 1L;

    public CategoryDoesNotExistException(String message) {
        super(message);
    }
}
