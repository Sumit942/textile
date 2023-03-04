package com.example.textile.controller;

import com.example.textile.action.EmployeeSubmitAction;
import com.example.textile.command.EmployeeCommand;
import com.example.textile.constants.CommandConstants;
import com.example.textile.enums.ActionType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.service.EmployeeService;
import com.example.textile.utility.factory.ActionExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

//@Controller
//@RequestMapping("/employees")
@Slf4j
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    private Map<String, ActionExecutor> actionExecutorMap;

    @PostConstruct
    public void init(){
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(EmployeeController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new EmployeeSubmitAction(employeeService));
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(dateFormat,true);
        dataBinder.registerCustomEditor(Date.class, dateEditor);
    }

    @GetMapping
    public ModelAndView viewEmployee(@ModelAttribute(CommandConstants.EMPLOYEE_COMMAND) EmployeeCommand employee) {
        ModelAndView modelAndView = new ModelAndView("/employees");
        ActionExecutor actionExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());

        try {
            actionExecutor.prePopulateOptionsAndFields(employee, modelAndView);
        } catch (InvalidObjectPopulationException e) {
            log.error("Exception in populating employeeCommand "+ e.getLocalizedMessage(),e);
        }

        return modelAndView;
    }

}
