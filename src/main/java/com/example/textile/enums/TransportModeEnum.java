package com.example.textile.enums;

public enum TransportModeEnum {
    ROAD("ROAD"),WATER("WATER"),AIR("AIR");

    final String mode;

    TransportModeEnum(String mode) {
        this.mode = mode;
    }

    public String getTransportMode() {
        return mode;
    }
}
