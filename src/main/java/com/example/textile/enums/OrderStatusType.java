package com.example.textile.enums;

public enum OrderStatusType {
    RECEIVED("Yarn Received"),
    IN_PROCESS("Yarn Loaded to Machine"),
    PROCESSED("Yarn-Fabric Rolls Created"),
    HALF_DELIVERED("Some Order/Yarn-Fabric Dispatched"),
    DELIVERED("Total Order/Yarn-Fabric Dispatched"),
    CANCELLED("Order/Yarn Cancelled"),
    HOLD("Order in put on Hold");

    final String message;
    OrderStatusType(String message) {
        this.message = message;
    }
    String getMessage() {
        return message;
    }
}
