package com.example.textile.executors;

import com.example.textile.enums.ResponseType;
import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionResponse {

    @Getter
    private ResponseType responseType;

    @Getter
    private List<ObjectError> errorList = null;

    private List<String> errors;

    @Getter
    private Object dbObj = null;

    public ActionResponse(ResponseType responseType) {
        this.responseType = responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public void setErrorList(List<ObjectError> errorList) {
        this.errorList = errorList;
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
