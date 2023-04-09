package com.example.textile.action;

import com.example.textile.command.StatementCommand;
import com.example.textile.entity.Employee;
import com.example.textile.entity.Statement;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.StatementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class StatementSubmitAction extends ActionExecutor<StatementCommand> {

    private StatementService statementService;

    public StatementSubmitAction(StatementService statementService) {
        this.statementService = statementService;
    }

    @Override
    protected ActionResponse onSuccess(StatementCommand statementCommand, Map<String, Object> parameterMap, ModelMap model) {
        return null;
    }

    @Override
    protected void doValidation(StatementCommand statementCommand, Map<String, Object> parameterMap, BindingResult result, ModelMap model) {
        String logPrefix = "doValidation() | ";
        String logSuffix = "";
        log.info("{} Entry", logPrefix);
        Map<String,String> errMap = new HashMap<>();
        List<Statement> statements = statementCommand.getStatements();
        if (statements != null && !statements.isEmpty()) {
            for (int i=0; i < statements.size(); i++) {

                if (statements.get(i).getAmount() == null || statements.get(i).getAmount().intValue() <= 0) {
                    errMap.put("statement["+i+"].amount","NotNull.statements.amount");
                }
                if (statements.get(i).getEmployee() == null || statements.get(i).getEmployee().getId() <= 0) {
                    errMap.put("statement["+i+"].employee","NotNull.statements.employee");
                }
                if (statements.get(i).getTxnDt() == null) {

                }
            }
        }

    }

    @Override
    protected void doPreSaveOperation(StatementCommand statementCommand, BindingResult result) {

    }

    @Override
    public void prePopulateOptionsAndFields(StatementCommand statementCommand, Object model) throws InvalidObjectPopulationException {
        if(!(model instanceof ModelMap)) {
            throw new InvalidObjectPopulationException(statementCommand,model);
        }
        ModelMap modelView = (ModelMap) model;

        List<Employee> employees = statementService.findAllEmployees();
        List<Statement> statements = statementService.findAllOrderByAndLimit("txnDt",0,20);
        modelView.addAttribute("employees",employees);
        modelView.addAttribute("statements",statements);
    }
}
