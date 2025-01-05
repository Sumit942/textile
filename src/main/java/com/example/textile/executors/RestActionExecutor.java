package com.example.textile.executors;

import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.exception.ServiceActionException;
import com.example.textile.utility.ShreeramTextileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.Map;

@Slf4j
public abstract class RestActionExecutor<T> extends ActionExecutor<T> {

    protected abstract ActionResponse onSuccessRest(T t, Map<String, Object> parameterMap);

    protected abstract void doValidationRest(T t, Map<String, Object> parameterMap, Map<String, String> errorMap);

    protected abstract void doPreSaveOperationRest(T t, Map<String, Object> parameterMap, Map<String, String> errorMap);

    private ActionResponse onErrorRest(T t, ActionResponse actionResponse, Map<String, Object> parameterMap, Map<String, String> errorMap) {
        if (actionResponse == null) {
            actionResponse = new ActionResponse(ResponseType.FAILURE);
        }
        return actionResponse;
    }

    public ActionResponse executeRest(T t, Map<String, Object> parameters, Map<String, String> errorMap) {
        ActionType actionType = (ActionType) parameters.get(ShreeramTextileConstants.ACTION);
        ActionResponse actionResponse = null;

        if (actionType.equals(ActionType.SUBMIT)) {
            doValidationRest(t, parameters, errorMap);
            if (!errorMap.isEmpty()) {
                log.error("Error performing: doValidationRest(): count-" + errorMap.size());
                return onErrorRest(t, actionResponse, parameters, errorMap);
            }
        }

        try {
            doPreSaveOperationRest(t, parameters, errorMap);
        } catch (Exception e) {
            log.error("Error performing: doPreSaveOperationRest(): " + e.getLocalizedMessage(), e);
            errorMap.put("preSave","System.Exception.DB");
            return onErrorRest(t, actionResponse, parameters, errorMap);
        }
        if (!errorMap.isEmpty()) {
            log.error("Error: errorMap notEmpty from doPreSaveOperationRest");
            return onErrorRest(t, actionResponse, parameters, errorMap);
        }
        return onSuccessRest(t, parameters);
    }

    @Override
    public ActionResponse execute(T t, Map<String, Object> parameterMap, BindingResult result, ModelMap model) throws ServiceActionException {
        throw new UnsupportedOperationException();
    }

    @Override
    protected ActionResponse onSuccess(T t, Map<String, Object> parameterMap, ModelMap model) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doValidation(T t, Map<String, Object> parameterMap, BindingResult result, ModelMap model) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doPreSaveOperation(T t, BindingResult result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void prePopulateOptionsAndFields(T t, Object model) throws InvalidObjectPopulationException {
        throw new UnsupportedOperationException();
    }
}