package com.example.textile.utility;

import java.util.Objects;

public class ActionValidationUtil {

    public static boolean isNullOrLessThanOne(Long id) {
        return Objects.isNull(id) || id.compareTo(0L) <= 0;
    }

    public static boolean longEquals(Long thisOne, Long thatOne) {
        return Objects.equals(thisOne, thatOne) || (thisOne != null && thatOne !=null && thisOne.compareTo(thatOne) == 0);
    }
}
