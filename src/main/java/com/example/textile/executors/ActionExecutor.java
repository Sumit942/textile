package com.example.textile.executors;

import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.exception.ServiceActionException;
import com.example.textile.utility.ShreeramTextileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.Map;

@Slf4j
public abstract class ActionExecutor<T> {

    protected abstract ActionResponse onSuccess(T t, Map<String, Object> parameterMap, ModelMap model);

    protected abstract void doValidation(T t, Map<String, Object> parameterMap, BindingResult result, ModelMap model);

    private ActionResponse onError(T t,BindingResult result, ActionResponse actResponse, ModelMap model) throws ServiceActionException {
        prePopulateOptionsAndFields(t,model);
        if (actResponse == null)
            actResponse = new ActionResponse(ResponseType.FAILURE);
        actResponse.setErrorList(result.getAllErrors());
        return actResponse;
    }

    @Transactional
    public ActionResponse execute(T t, Map<String, Object> parameterMap, BindingResult result, ModelMap model) throws ServiceActionException {

        ActionType action = (ActionType) parameterMap.get(ShreeramTextileConstants.ACTION);
        ActionResponse actionResponse = null;

        if (ActionType.SUBMIT.equals(action)) {
            //if result already has error: before doValidation()
            if (result.hasErrors())
                return onError(t, result, actionResponse,model);

            try {
                doValidation(t, parameterMap, result, model);
                if (result.hasErrors())
                    return onError(t, result, actionResponse,model);
            } catch (RuntimeException e) {
                log.error("Error: exception in doValidation() - "+e.getLocalizedMessage(),e);
                return onError(t, result, actionResponse,model);
            }
        }
        try {
            doPreSaveOperation(t, result);
        } catch (Exception e) {
            log.error("Error: doPreSaveOperation() | "+e.getLocalizedMessage(),e);
            result.reject("System.Exception.DB");
            return onError(t,result,actionResponse,model);
        }
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
     * @param model ModalMap
     * @throws InvalidObjectPopulationException when wrong command Object is passed
     */
    public abstract void prePopulateOptionsAndFields(T t, Object model) throws InvalidObjectPopulationException;
}
