package com.example.textile.executors;

import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.exception.ServiceActionException;
import com.example.textile.utility.ShreeramTextileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Slf4j
public abstract class ActionExecutor<T> {

    protected abstract ActionResponse onSuccess(T t, Map<String, Object> parameterMap, ModelAndView model);

    protected abstract void doValidation(T t, Map<String, Object> parameterMap, BindingResult result, ModelAndView model);

    private ActionResponse onError(T t,BindingResult result, ActionResponse actResponse, ModelAndView model) throws ServiceActionException {
        prePopulateOptionsAndFields(t,model);
        if (actResponse == null)
            actResponse = new ActionResponse(ResponseType.FAILURE);
        actResponse.setErrorList(result.getAllErrors());
        return actResponse;
    }

    public ActionResponse execute(T t, Map<String, Object> parameterMap, BindingResult result, ModelAndView model) throws ServiceActionException {

        ActionType action = (ActionType) parameterMap.get(ShreeramTextileConstants.ACTION);
        ActionResponse actionResponse = null;

        if (ActionType.SUBMIT.equals(action)) {
            //if result already has error: before doValidation()
            if (result.hasErrors()) {
                return onError(t, result, actionResponse,model);
            }

            try {
                doValidation(t, parameterMap, result, model);
                if (result.hasErrors()) {
                    return onError(t, result, actionResponse,model);
                }
            } catch (RuntimeException e) {
                log.error("Error: exception in doValidation() - "+e.getLocalizedMessage(),e);
                return onError(t, result, actionResponse,model);
            }
        }
        doPreSaveOperation(t,result);
        if (result.hasErrors()) {
            log.error("Error: result.hasErrors from doPreSaveOperation");
            return onError(t, result, actionResponse,model);
        }
        return onSuccess(t, parameterMap, model);
    }

    protected abstract void doPreSaveOperation(T t, BindingResult result);

    /**
     * This will do all the preliminary work : population all the pre required inputs and dropdowns.
     * @param t Entity
     * @param model modelAndView
     * @throws InvalidObjectPopulationException when wrong command Object is passed
     */
    public abstract void prePopulateOptionsAndFields(T t, Object model) throws InvalidObjectPopulationException;
}
