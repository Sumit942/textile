package com.example.textile.exception;

public class InvoiceNotFoundException extends RuntimeException {
    public InvoiceNotFoundException(String s) {
        super("Not Invoice Found by id"+s);
    }
}
