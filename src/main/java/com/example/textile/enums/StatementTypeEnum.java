package com.example.textile.enums;

public enum StatementTypeEnum {
    CREDIT(1),DEBIT(-1);
    private final int value;
    StatementTypeEnum(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return this.value;
    }
}
