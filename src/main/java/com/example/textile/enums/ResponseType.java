package com.example.textile.enums;

public enum ResponseType {
    SUCCESS("SUCCESS"),FAILURE("FAILURE");

    private final String actResponse;

    ResponseType(String actResponse) {
        this.actResponse = actResponse;
    }

    public String getActResponse() {
        return actResponse;
    }
}
