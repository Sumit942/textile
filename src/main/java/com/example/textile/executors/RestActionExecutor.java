package com.example.textile.executors;

import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.exception.ServiceActionException;
import com.example.textile.utility.Constants;
import com.example.textile.utility.ShreeramTextileConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.Map;

public abstract class RestActionExecutor<T> extends ActionExecutor<T> {

    private static final Logger log = LoggerFactory.getLogger(RestActionExecutor.class);

    protected abstract ActionResponse<T> onSuccessRest(T t, Map<String, Object> parameterMap);

    protected abstract void doValidationRest(T t, Map<String, Object> parameterMap, Map<String, String[]> errorMap);

    protected abstract void doPreSaveOperationRest(T t, Map<String, Object> parameterMap, Map<String, String[]> errorMap);

    private ActionResponse<T> onErrorRest(T t, ActionResponse<T> actionResponse, Map<String, Object> parameterMap, Map<String, String[]> errorMap) {
        if (actionResponse == null) {
            actionResponse = new ActionResponse<>(ResponseType.FAILURE);
        }
        return actionResponse;
    }

    public ActionResponse<T> executeRest(T t, Map<String, Object> parameters, Map<String, String[]> errorMap) {
        log.debug("executeRest() Entry [{}]", t);
        ActionType actionType = (ActionType) parameters.get(ShreeramTextileConstants.ACTION);
        ActionResponse<T> actionResponse = null;

        if (ActionType.SUBMIT.equals(actionType)) {
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
            errorMap.put("preSave", new String[]{"System.Exception.DB"});
            return onErrorRest(t, actionResponse, parameters, errorMap);
        }
        if (!errorMap.isEmpty()) {
            log.error("Error: errorMap notEmpty from doPreSaveOperationRest");
            return onErrorRest(t, actionResponse, parameters, errorMap);
        }
        return onSuccessRest(t, parameters);
    }

    @Override
    public ActionResponse<T> execute(T t, Map<String, Object> parameterMap, BindingResult result, ModelMap model) throws ServiceActionException {
        throw new UnsupportedOperationException(Constants.notSupportedRest);
    }

    @Override
    protected ActionResponse<T> onSuccess(T t, Map<String, Object> parameterMap, ModelMap model) {
        throw new UnsupportedOperationException(Constants.notSupportedRest);
    }

    @Override
    protected void doValidation(T t, Map<String, Object> parameterMap, BindingResult result, ModelMap model) {
        throw new UnsupportedOperationException(Constants.notSupportedRest);
    }

    @Override
    protected void doPreSaveOperation(T t, BindingResult result) {
        throw new UnsupportedOperationException(Constants.notSupportedRest);
    }

    @Override
    public void prePopulateOptionsAndFields(T t, Object model) throws InvalidObjectPopulationException {
        throw new UnsupportedOperationException(Constants.notSupportedRest);
    }
}