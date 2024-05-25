package com.suyash.employeeservice.exception;

public class DepartmentNotFoundException extends ResourceNotFoundException{
    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
