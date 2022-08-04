package com.example.textile.executors;

import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.exception.ServiceActionException;
import com.example.textile.utility.ShreeramTextileConstants;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public abstract class ActionExecutor<T> {

    protected abstract ActionResponse onSuccess(T t, Map<String, Object> parameterMap, ModelAndView model);

    protected abstract void doValidation(T t, Map<String, Object> parameterMap, BindingResult result, ModelAndView model);

    private ActionResponse onError(T t,BindingResult result, ActionResponse actResponse, ModelAndView model) throws ServiceActionException {
        prePopulateOptionsAndFields(t,model);
        actResponse.setErrorList(result.getAllErrors());
        return actResponse;
    }

    public ActionResponse execute(T t, Map<String, Object> parameterMap, BindingResult result, ModelAndView model) throws ServiceActionException {

        ActionType action = (ActionType) parameterMap.get(ShreeramTextileConstants.ACTION);

        if (ActionType.SUBMIT.equals(action)) {
            doValidation(t, parameterMap, result,model);
            if (result.hasErrors()) {
                ActionResponse actionResponse = new ActionResponse(ResponseType.FAILURE);
                return onError(t, result, actionResponse,model);
            }
        }
        return onSuccess(t, parameterMap, model);
    }

    /**
     * This will do all the preliminary work : population all the pre required inputs and dropdowns.
     * @param t
     * @param model
     * @throws InvalidObjectPopulationException when wrong command Object is passed
     */
    public abstract void prePopulateOptionsAndFields(T t, Object model) throws InvalidObjectPopulationException;
}
