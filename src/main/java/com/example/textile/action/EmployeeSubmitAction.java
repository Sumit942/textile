package com.example.textile.action;

import com.example.textile.command.EmployeeCommand;
import com.example.textile.entity.Designation;
import com.example.textile.entity.Employee;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.EmployeeService;
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
        return null;
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
            if (employee.getAccountNo() == null || employee.getAccountNo().isEmpty())
                errMap.put("employee.accountNo","NotNull.employeeCommand.employee.accountNo");
            if (employee.getIfscCode() == null || employee.getIfscCode().isEmpty())
                errMap.put("employee.ifscCode","NotNull.employeeCommand.employee.ifscCode");
            if (employee.getPanCardNo() == null || employee.getPanCardNo().isEmpty())
                errMap.put("employee.panCardNo","NotNull.employeeCommand.employee.panCardNo");
            if (employee.getAddress() == null || employee.getAddress().getAddress() == null)
                errMap.put("employee.address.address","NotNull.employeeCommand.employee.address.address");

            if (errMap.isEmpty()) {
                Employee savedEmployee = employeeService.findByPanCard(employeeCommand.getEmployee().getPanCardNo());
                if (savedEmployee != null)
                    errMap.put("duplicate.employee","duplicate.employeeCommand.employee");
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

    }

    @Override
    public void prePopulateOptionsAndFields(EmployeeCommand employeeCommand, Object model) throws InvalidObjectPopulationException {
        ModelAndView modelAndView = null;

        if(!(model instanceof ModelMap)) {
            throw new InvalidObjectPopulationException(employeeCommand,model);
        }
        ModelMap modelView = (ModelMap) model;

        List<Employee> employees = employeeService.findAll();
        List<Designation> designations = employeeService.findAllDesignations();

        modelAndView.addObject("employees",employees);
        modelAndView.addObject("designations", designations);
    }
}
