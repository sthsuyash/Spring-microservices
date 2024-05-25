package com.suyash.departmentservice.exception;

public class DepartmentNotFoundException extends ResourceNotFoundException{
    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
