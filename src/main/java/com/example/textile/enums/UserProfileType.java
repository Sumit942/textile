package com.example.textile.enums;

import java.io.Serializable;

public enum UserProfileType implements Serializable {
    USER("USER"), ADMIN("ADMIN"), DBA("DBA");

    final String userProfileType;

    UserProfileType(String userProfileType) {
        this.userProfileType = userProfileType;
    }

    public String getUserProfileType() {
        return userProfileType;
    }
}
