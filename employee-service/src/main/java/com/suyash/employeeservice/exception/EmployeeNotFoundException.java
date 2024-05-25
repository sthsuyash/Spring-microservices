package com.suyash.employeeservice.exception;

public class EmployeeNotFoundException extends ResourceNotFoundException{
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
