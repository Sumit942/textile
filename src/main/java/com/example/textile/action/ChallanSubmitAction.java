package com.example.textile.action;

import com.example.textile.command.ChallanCommand;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.ChallanService;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.Map;

public class ChallanSubmitAction extends ActionExecutor<ChallanCommand> {

    private final ChallanService challanService;

    public ChallanSubmitAction(ChallanService challanService) {
        this.challanService = challanService;
    }

    @Override
    protected ActionResponse onSuccess(ChallanCommand challanCommand, Map<String, Object> parameterMap, ModelMap model) {
        return null;
    }

    @Override
    protected void doValidation(ChallanCommand challanCommand, Map<String, Object> parameterMap, BindingResult result, ModelMap model) {

    }

    @Override
    protected void doPreSaveOperation(ChallanCommand challanCommand, BindingResult result) {

    }

    @Override
    public void prePopulateOptionsAndFields(ChallanCommand challanCommand, Object model) throws InvalidObjectPopulationException {

    }
}
