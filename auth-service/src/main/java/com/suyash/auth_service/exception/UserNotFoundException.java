package com.suyash.auth_service.exception;

public class UserNotFoundException extends ResourceNotFoundException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
