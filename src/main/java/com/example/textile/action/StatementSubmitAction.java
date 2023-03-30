package com.example.textile.action;

import com.example.textile.command.StatementCommand;
import com.example.textile.entity.Statement;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.Map;

public class StatementSubmitAction extends ActionExecutor<StatementCommand> {

    @Override
    protected ActionResponse onSuccess(StatementCommand statement, Map<String, Object> parameterMap, ModelMap model) {
        return null;
    }

    @Override
    protected void doValidation(StatementCommand statement, Map<String, Object> parameterMap, BindingResult result, ModelMap model) {

    }

    @Override
    protected void doPreSaveOperation(StatementCommand statement, BindingResult result) {

    }

    @Override
    public void prePopulateOptionsAndFields(StatementCommand statement, Object model) throws InvalidObjectPopulationException {

    }
}
