package com.example.textile.utility;

import org.springframework.context.MessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FactoryUtility {

    public static Map<String, String> convertToErrorMsg(Map<String, String> errorMap, MessageSource messageSource, HttpServletRequest request) {
        Map<String, String> errorMessages = new HashMap<>();
        if (!errorMap.isEmpty()) {
            errorMap.forEach((field, errCode) -> {
                String errMsg = messageSource.getMessage(errCode, null, request == null ? Locale.ENGLISH : request.getLocale());
                errorMessages.put(field, errMsg);
            });
        }
        return errorMessages;
    }
}
