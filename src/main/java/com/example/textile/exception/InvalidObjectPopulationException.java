package com.example.textile.exception;

public class InvalidObjectPopulationException extends ServiceActionException {

    public InvalidObjectPopulationException(Object enitity, Object model) {
        super(model.getClass().getName() + " cannot be used for population of " + enitity.getClass().getName() + " entity");

    }
}
