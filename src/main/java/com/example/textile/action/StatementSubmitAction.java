package com.example.textile.action;

import com.example.textile.command.StatementCommand;
import com.example.textile.constants.TextileConstants;
import com.example.textile.entity.BankStatement;
import com.example.textile.entity.Employee;
import com.example.textile.entity.User;
import com.example.textile.enums.ResponseType;
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

    private final StatementService statementService;

    public StatementSubmitAction(StatementService statementService) {
        this.statementService = statementService;
    }

    @Override
    protected ActionResponse onSuccess(StatementCommand statementCommand, Map<String, Object> parameterMap, ModelMap model) {
        String logPrefix = "onSuccess() | ";
        log.info("{} Entry",logPrefix);
        User user = (User) parameterMap.get(TextileConstants.USER);
        statementCommand.getStatements().forEach(e -> e.setUser(user));
        List<BankStatement> statements = statementService.saveAll(statementCommand.getStatements());
        statementCommand.setStatements(statements);
        ActionResponse actionResponse = new ActionResponse(ResponseType.SUCCESS);
        actionResponse.setDbObj(statementCommand);
        return actionResponse;
    }

    @Override
    protected void doValidation(StatementCommand statementCommand, Map<String, Object> parameterMap, BindingResult result, ModelMap model) {
        String logPrefix = "doValidation() | ";
        String logSuffix = "";
        log.info("{} Entry", logPrefix);
        Map<String,String> errMap = new HashMap<>();
        List<BankStatement> statements = statementCommand.getStatements();
        if (statements != null && !statements.isEmpty()) {
            for (int i=0; i < statements.size(); i++) {

                if (statements.get(i).getTxnDt() == null) {
                    errMap.put("statement["+i+"].txnDt","NotNull.statement.txnDt");
                }
                if (statements.get(i).getAmount() == null || statements.get(i).getAmount().intValue() <= 0) {
                    errMap.put("statement["+i+"].amount","NotNull.statement.amount");
                }
                if (statements.get(i).getDescription() == null || statements.get(i).getDescription().isEmpty()) {
                    errMap.put("statement["+i+"].remarks","NotNull.statement.remark");
                }
                /**  TODO: add column for employee in statement later
                if (statements.get(i).getEmployee() == null || statements.get(i).getEmployee().getId() <= 0) {
                    errMap.put("statement["+i+"].employee","NotNull.statements.employee");
                }*/
            }
        }

        errMap.forEach(result::rejectValue);
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
        List<BankStatement> statements = statementService.findAllOrderByAndLimit("txnDt",0,20);
        modelView.addAttribute("employees",employees);
        modelView.addAttribute("statements",statements);
    }
}
