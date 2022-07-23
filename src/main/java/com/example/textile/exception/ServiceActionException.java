package com.example.textile.exception;

public class ServiceActionException extends Throwable {

    public ServiceActionException(String message) {
        super("Exception: Layer Service" + message);
    }

}
