package com.ftn.sbnz.model.models.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("User not found.");
    }
}
