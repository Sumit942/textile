package com.example.textile.action;

import com.example.textile.constants.TextileConstants;
import com.example.textile.entity.User;
import com.example.textile.entity.YarnDeo;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.YarnDeoService;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.Map;

public class YarnDeoSubmitAction extends ActionExecutor<YarnDeo> {

    private YarnDeoService yarnDeoService;

    public YarnDeoSubmitAction(YarnDeoService yarnDeoService) {
        this.yarnDeoService = yarnDeoService;
    }

    @Override
    protected ActionResponse onSuccess(YarnDeo yarnDeo, Map<String, Object> parameterMap, ModelMap model) {

        User user = (User) parameterMap.get(TextileConstants.USER);
        yarnDeo.setUser(user);

        yarnDeoService.saveOrUpdate(yarnDeo);
        ActionResponse actionResponse = new ActionResponse(ResponseType.SUCCESS);
        return actionResponse;
    }

    @Override
    protected void doValidation(YarnDeo yarnDeo, Map<String, Object> parameterMap, BindingResult result, ModelMap model) {

    }

    @Override
    protected void doPreSaveOperation(YarnDeo yarnDeo, BindingResult result) {

    }

    @Override
    public void prePopulateOptionsAndFields(YarnDeo yarnDeo, Object model) throws InvalidObjectPopulationException {

    }
}
