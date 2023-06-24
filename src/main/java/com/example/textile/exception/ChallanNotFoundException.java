package com.example.textile.exception;

public class ChallanNotFoundException extends RuntimeException {

    public ChallanNotFoundException(Object id) {
        super("challan not found ["+id+"]");
    }
}
