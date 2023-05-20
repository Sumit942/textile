package com.example.textile.action;

import com.example.textile.command.SalaryStatementCommand;
import com.example.textile.constants.TextileConstants;
import com.example.textile.entity.BankStatement;
import com.example.textile.entity.Employee;
import com.example.textile.entity.SalaryStatement;
import com.example.textile.entity.User;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.SalaryStatementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public class SalaryStatementSubmitAction extends ActionExecutor<SalaryStatementCommand> {

    private final SalaryStatementService statementService;

    public SalaryStatementSubmitAction(SalaryStatementService salaryStatementService) {
        this.statementService = salaryStatementService;
    }

    @Override
    protected ActionResponse onSuccess(SalaryStatementCommand salaryStatementCommand, Map<String, Object> parameterMap, ModelMap model) {
        String logPrefix = "onSuccess() | ";
        log.info("{} Entry",logPrefix);
        User user = (User) parameterMap.get(TextileConstants.USER);
        salaryStatementCommand.getStatements().forEach(e -> e.setUser(user));
        List<SalaryStatement> statements = statementService.saveAll(salaryStatementCommand.getStatements());
        salaryStatementCommand.setStatements(statements);
        ActionResponse actionResponse = new ActionResponse(ResponseType.SUCCESS);
        actionResponse.setDbObj(salaryStatementCommand);
        return actionResponse;
    }

    @Override
    protected void doValidation(SalaryStatementCommand salaryStatementCommand, Map<String, Object> parameterMap, BindingResult result, ModelMap model) {
        String logPrefix = "doValidation() | ";
        String logSuffix = "";
        log.info("{} Entry", logPrefix);
        Map<String,String> errMap = new HashMap<>();
        List<SalaryStatement> statements = salaryStatementCommand.getStatements();
        if (statements != null && !statements.isEmpty()) {
            for (int i=0; i < statements.size(); i++) {

                if (statements.get(i).getTxnDt() == null) {
                    errMap.put("statement["+i+"].txnDt","NotNull.statement.txnDt");
                }
                if (statements.get(i).getAmount() == null || statements.get(i).getAmount().intValue() <= 0) {
                    errMap.put("statement["+i+"].amount","NotNull.statement.amount");
                }
                if (statements.get(i).getDescription() == null || statements.get(i).getDescription().isEmpty()) {
                    errMap.put("statement["+i+"].description","NotNull.statement.description");
                }
                if (statements.get(i).getPaymentMode() == null || statements.get(i).getPaymentMode().isEmpty()) {
                    errMap.put("statement["+i+"].paymentMode","NotNull.statement.paymentMode");
                }
                if (statements.get(i).getName() == null || statements.get(i).getName().isEmpty()) {
                    errMap.put("statement["+i+"].name","NotNull.statement.name");
                }
                if (statements.get(i).getDebitFrom().getId() == null || statements.get(i).getDebitFrom().getId() <= 0) {
                    errMap.put("statement["+i+"].debitFrom.id","NotNull.statement.debitFrom.id");
                }
                if (statements.get(i).getEmployee() == null || statements.get(i).getEmployee().getId() <= 0) {
                    errMap.put("statement["+i+"].employee.id","NotNull.statements.employee.id");
                }
            }
        }

        errMap.forEach(result::rejectValue);
    }

    @Override
    protected void doPreSaveOperation(SalaryStatementCommand salaryStatementCommand, BindingResult result) {

    }

    @Override
    public void prePopulateOptionsAndFields(SalaryStatementCommand salaryStatementCommand, Object model) throws InvalidObjectPopulationException {
        if(!(model instanceof ModelMap)) {
            throw new InvalidObjectPopulationException(salaryStatementCommand,model);
        }
        ModelMap modelView = (ModelMap) model;

        List<Employee> employees = statementService.findAllEmployees();
        List<SalaryStatement> statements = statementService.findAllOrderByAndLimit("txnDt",0,20);
        modelView.addAttribute("employees",employees);
        modelView.addAttribute("statements",statements);
    }

}
