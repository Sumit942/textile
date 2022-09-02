package com.example.textile.enums;

public enum ActionType {
    SUBMIT("SUBMIT"),SAVE("SAVE");

    final String actionType;

    ActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionType() {
        return actionType;
    }
}
