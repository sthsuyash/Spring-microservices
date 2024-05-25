package com.suyash.departmentservice.exception;

public class EmployeesNotFoundException extends ResourceNotFoundException{
    public EmployeesNotFoundException(String message) {
        super(message);
    }
}
