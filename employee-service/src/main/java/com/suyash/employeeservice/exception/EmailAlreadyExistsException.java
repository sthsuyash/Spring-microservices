package com.suyash.employeeservice.exception;

/**
 * Exception thrown when an email already exists.
 */
public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
