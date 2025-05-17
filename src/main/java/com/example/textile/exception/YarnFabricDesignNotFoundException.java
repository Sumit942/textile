package com.example.textile.exception;

public class YarnFabricDesignNotFoundException extends RuntimeException {

    private static final String message = "YarnFabricDesign NotFound by id: ";

    public YarnFabricDesignNotFoundException(Long id) {
        super(message+id);
    }
}
