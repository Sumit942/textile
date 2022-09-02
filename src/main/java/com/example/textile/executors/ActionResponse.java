package com.example.textile.executors;

import com.example.textile.enums.ResponseType;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionResponse {

    private ResponseType responseType;

    private List<ObjectError> errorList = null;

    private List<String> errors;

    private Object dbObj = null;

    public ActionResponse(ResponseType responseType) {
        this.responseType = responseType;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public List<ObjectError> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<ObjectError> errorList) {
        this.errorList = errorList;
    }

    public Object getDbObj() {
        return dbObj;
    }

    public void setDbObj(Object dbObj) {
        this.dbObj = dbObj;
    }

    public List<String> getErrors() {
        if (errors == null)
            errors = new ArrayList<>();
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addErrorMessage(String message) {
        getErrors().add(message);
    }
}
