package com.example.textile.enums;

public enum UnitOfMeasureEnum {
    KG("KG"),MTS("Meters");
    private final String unit;

    UnitOfMeasureEnum(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
