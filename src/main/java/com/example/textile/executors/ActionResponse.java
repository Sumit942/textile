package com.example.textile.executors;

import com.example.textile.enums.ResponseType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class ActionResponse<T> {

    @Getter
    private ResponseType responseType;

    @Getter
    private List<ObjectError> errorList = null;

    private List<String> errors;

    @Getter
    private T dbObj = null;

    public ActionResponse(ResponseType responseType) {
        this.responseType = responseType;
    }

    public List<String> getErrors() {
        if (errors == null)
            errors = new ArrayList<>();
        return errors;
    }

    public void addErrorMessage(String message) {
        getErrors().add(message);
    }
}
