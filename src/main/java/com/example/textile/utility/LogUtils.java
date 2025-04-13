package com.example.textile.utility;

public class LogUtils {
    private LogUtils() {
    }

    public static String createEntryLog(String methodName) {
        return String.format("%s Entry", methodName);
    }

    public static String createNameValue(String key, Object value) {
        return String.format("%s=[%s]", key, value);
    }

    public static String createExitLog(String methodName, String logSuffix) {
        return String.format("%s Exit [%s]", methodName, logSuffix);
    }
}
