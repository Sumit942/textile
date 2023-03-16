package com.example.textile.controller;

import com.example.textile.action.EmployeeSubmitAction;
import com.example.textile.command.EmployeeCommand;
import com.example.textile.constants.CommandConstants;
import com.example.textile.constants.TextileConstants;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.exception.ServiceActionException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.EmployeeService;
import com.example.textile.utility.ShreeramTextileConstants;
import com.example.textile.utility.factory.ActionExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/employees")
@Slf4j
public class EmployeeController extends BaseController {

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
    public ModelAndView viewEmployee(@ModelAttribute(CommandConstants.EMPLOYEE_COMMAND) EmployeeCommand employee, ModelMap model) {
        ModelAndView modelAndView = new ModelAndView("/employees");
        ActionExecutor actionExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());

        try {
            actionExecutor.prePopulateOptionsAndFields(employee, model);
        } catch (InvalidObjectPopulationException e) {
            log.error("Exception in populating employeeCommand "+ e.getLocalizedMessage(),e);
        }

        return modelAndView;
    }

    @PostMapping
    public ModelAndView submitEmployee(@Valid @ModelAttribute(CommandConstants.EMPLOYEE_COMMAND) EmployeeCommand employee, BindingResult result,
                                       ModelMap model, RedirectAttributes redirectAttributes) throws InvalidObjectPopulationException {
        String logPrefix = "submitEmployee() ";
        log.info("{} | Entry",logPrefix);
        ModelAndView modelAndView = new ModelAndView("/employees");
        ActionExecutor actionExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);
        parameterMap.put(TextileConstants.USER, getLoggedInUser());

        try {
            ActionResponse execute = actionExecutor.execute(employee, parameterMap, result, model);
            if (ResponseType.SUCCESS.equals(execute.getResponseType())) {
                logPrefix += "ResponseType=SUCCESS";
            } else {
                logPrefix += "ResponseType=Error";
                log.error("{} result validation error", logPrefix);
            }
            log.info("{} | Complete", logPrefix);
        } catch (DataAccessException e) {
            actionExecutor.prePopulateOptionsAndFields(employee, model);
            log.error("DB_Error in saving employee: "+e, e);
        } catch (Throwable e) {
            actionExecutor.prePopulateOptionsAndFields(employee, model);
            log.error("Exception in saving employee: "+e,e);
        }

        return modelAndView;
    }

}
