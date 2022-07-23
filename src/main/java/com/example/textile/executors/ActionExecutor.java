package com.example.textile.executors;

import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.utility.ShreeramTextileConstants;
import org.springframework.validation.BindingResult;

import java.util.Map;

public abstract class ActionExecutor<T> {

    protected abstract ActionResponse onSuccess(T t, Map<String, Object> parameterMap);

    protected abstract void doValidation(T t, Map<String, Object> parameterMap, BindingResult result);

    private ActionResponse onError(BindingResult result, ActionResponse actResponse) {
        actResponse.setErrorList(result.getAllErrors());
        return actResponse;
    }

    public ActionResponse execute(T t, Map<String, Object> parameterMap, BindingResult result) {

        ActionType action = (ActionType) parameterMap.get(ShreeramTextileConstants.ACTION);

        if (ActionType.SUBMIT.equals(action)) {
            doValidation(t, parameterMap, result);
            if (result.hasErrors()) {
                ActionResponse actionResponse = new ActionResponse(ResponseType.FAILURE);
                return onError(result, actionResponse);
            }
        }
        return onSuccess(t, parameterMap);
    }

    /**
     * This will do all the preliminary work : population all the pre required inputs and dropdowns.
     * @param t
     * @param model
     * @throws InvalidObjectPopulationException
     */
    public abstract void prePopulateOptionsAndFields(T t, Object model) throws InvalidObjectPopulationException;
}
