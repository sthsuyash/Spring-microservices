package com.suyash.employeeservice.exception;

/**
 * Exception thrown when a department is not found.
 */
public class DepartmentNotFoundException extends ResourceNotFoundException{
    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
