package com.suyash.departmentservice.exception;

public class DepartmentAlreadyExistsException extends RuntimeException{
    public DepartmentAlreadyExistsException(String message){
        super(message);
    }
}
