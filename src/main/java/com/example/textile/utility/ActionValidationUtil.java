package com.example.textile.utility;

import java.math.BigDecimal;
import java.util.Objects;

public class ActionValidationUtil {

    public static boolean isNullOrLessThanOne(Long id) {
        return Objects.isNull(id) || id.compareTo(0L) < 1;
    }

    public static boolean longEquals(Long thisOne, Long thatOne) {
        return Objects.equals(thisOne, thatOne) || (thisOne != null && thatOne !=null && thisOne.compareTo(thatOne) == 0);
    }

    public static boolean isEqualToLessThanZero(Double value) {
        return Objects.isNull(value) || value.compareTo(0.00) <= 0;
    }

    public static boolean isEqualToLessThanZero(BigDecimal value) {
        return Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) <= 0;
    }
}
