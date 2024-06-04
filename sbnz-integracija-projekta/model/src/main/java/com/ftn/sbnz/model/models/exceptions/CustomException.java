package com.ftn.sbnz.model.models.exceptions;

public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }

    public CustomException() {
    }
}
