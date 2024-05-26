package com.suyash.employeeservice.exception;

/**
 * Exception thrown when an employee is not found.
 */
public class EmployeeNotFoundException extends ResourceNotFoundException{
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
