package com.suyash.review_service.exception;

/**
 * An exception that is thrown when an employee is not found.
 */
public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
