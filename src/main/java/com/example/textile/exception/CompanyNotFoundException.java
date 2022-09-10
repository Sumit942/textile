package com.example.textile.exception;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException(Long id) {
        super("company not found ["+id+"]");
    }
}
