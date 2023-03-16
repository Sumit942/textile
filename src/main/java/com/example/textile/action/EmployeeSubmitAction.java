package com.example.textile.action;

import com.example.textile.command.EmployeeCommand;
import com.example.textile.constants.TextileConstants;
import com.example.textile.entity.*;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.EmployeeService;
import com.example.textile.utility.ShreeramTextileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class EmployeeSubmitAction extends ActionExecutor<EmployeeCommand> {

    private final EmployeeService employeeService;

    public EmployeeSubmitAction(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    protected ActionResponse onSuccess(EmployeeCommand employeeCommand, Map<String, Object> parameterMap, ModelMap model) {
        String logPrefix = "onSuccess() | ";
        log.info("{} Entry", logPrefix);
        ActionType action = (ActionType) parameterMap.get(ShreeramTextileConstants.ACTION);
//        User user = (User) parameterMap.get(TextileConstants.USER);
        if (ActionType.SUBMIT.equals(action)) {
            Employee employee = employeeService.saveOrUpdate(employeeCommand.getEmployee());
            employeeCommand.setEmployee(employee);
        } else {

        }

        ActionResponse actionResponse = new ActionResponse(ResponseType.SUCCESS);
        log.info("{} Exit [Action={}, Response=SUCCESS, employeeID={}]",logPrefix,action.getActionType(),employeeCommand.getEmployee().getId());
        return actionResponse;
    }

    @Override
    protected void doValidation(EmployeeCommand employeeCommand, Map<String, Object> parameterMap, BindingResult result, ModelMap model) {
        String logPrefix = "doValidation() | ";
        String logSuffix = "";
        log.info("{} Entry", logPrefix);
        Map<String,String> errMap = new HashMap<>();
        Employee employee = employeeCommand.getEmployee();

        if (employee == null) {
            errMap.put("employee","NotNull.employeeCommand.employee");
            logSuffix+="Employee is null";
        } else {
            if (employee.getFirstName() == null || employee.getFirstName().isEmpty())
                errMap.put("employee.firstName","NotNull.employeeCommand.employee.firstName");
            if (employee.getLastName() == null || employee.getLastName().isEmpty())
                errMap.put("employee.lastName","NotNull.employeeCommand.employee.lastName");
            if (employee.getDesignation() == null || employee.getDesignation().getDesignation() == null
                    || employee.getDesignation().getDesignation().isEmpty())
                errMap.put("employee.designation.designation","NotNull.employeeCommand.employee.designation.designation");
            if (employee.getSalary() == null || employee.getSalary().intValue() <= 0)
                errMap.put("employee.salary","NotNull.employeeCommand.employee.salary");
            if (employee.getBankDetails() == null || employee.getBankDetails().isEmpty()){
                errMap.put("employee.bankDetail", "NotNull.employeeCommand.employee.bankDetail");
            } else {
                for (int i = 0; i < employee.getBankDetails().size(); i++){
                    BankDetail bankDetail = employee.getBankDetails().get(i);
                    if (bankDetail.getBankName() == null || bankDetail.getBankName().isEmpty())
                        errMap.put("employee.bankDetail["+i+"].bankName", "NotNull.employeeCommand.employee.bankDetail.bankName");
                    if (bankDetail.getBranch() == null || bankDetail.getBranch().isEmpty())
                        errMap.put("employee.bankDetail["+i+"].branch", "NotNull.employeeCommand.employee.bankDetail.branch");
                    if (bankDetail.getAccountNo() == null || bankDetail.getAccountNo().isEmpty())
                        errMap.put("employee.bankDetail["+i+"].accountNo", "NotNull.employeeCommand.employee.accountNo");
                    if (bankDetail.getIfsc() == null || bankDetail.getIfsc().isEmpty())
                        errMap.put("employee.bankDetail["+i+"].ifscCode", "NotNull.employeeCommand.employee.ifscCode");
                }

            }
            if (employee.getPanCardNo() == null || employee.getPanCardNo().isEmpty())
                errMap.put("employee.panCardNo","NotNull.employeeCommand.employee.panCardNo");
            if (employee.getAddress() == null || employee.getAddress().getAddress() == null || employee.getAddress().getAddress().isEmpty()) {
                errMap.put("employee.address.address", "NotNull.employeeCommand.employee.address.address");
            } else if (employee.getAddress().getState() == null || employee.getAddress().getState().getId() == null || employee.getAddress().getState().getId() <= 0) {
                errMap.put("employee.address.state", "NotNull.employeeCommand.employee.address.state");
            }
            if (errMap.isEmpty()) {
                Employee savedEmployee = employeeService.findByPanCard(employeeCommand.getEmployee().getPanCardNo());
                if (savedEmployee != null)
                    errMap.put("employee.panCardNo","duplicate.employeeCommand.employee");
            } else {
                logSuffix += ("validationError count ="+errMap.size());
            }
        }

        //adding validation error in Binding result
        errMap.forEach(result::rejectValue);

        log.info("{} {} | Exit",logPrefix,logSuffix);
    }

    @Override
    protected void doPreSaveOperation(EmployeeCommand employeeCommand, BindingResult result) {
        String logPrefix = "doPreSaveOperation() | ";
        String logSuffix = "";
//        Employee byPanCard = employeeService.findByPanCard(employeeCommand.getEmployee().getPanCardNo());
//        if (byPanCard != null) {
//            result.rejectValue("employee.panCardNo","employeeCommand.employee.panCardNo.alreadyExist",
//                    new Object[]{byPanCard.getFullName()},"Pan Card Already Exist");
//        }
    }

    @Override
    public void prePopulateOptionsAndFields(EmployeeCommand employeeCommand, Object model) throws InvalidObjectPopulationException {

        if(!(model instanceof ModelMap)) {
            throw new InvalidObjectPopulationException(employeeCommand,model);
        }
        ModelMap modelView = (ModelMap) model;

        List<Employee> employees = employeeService.findAll();
        List<Designation> designations = employeeService.findAllDesignations();
        List<State> states = employeeService.findAllStates();

        modelView.addAttribute("employees",employees);
        modelView.addAttribute("designations", designations);
        modelView.addAttribute("states", states);
    }
}
