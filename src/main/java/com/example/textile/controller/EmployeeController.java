package com.example.textile.controller;

import com.example.textile.action.EmployeeSubmitAction;
import com.example.textile.command.EmployeeCommand;
import com.example.textile.constants.CommandConstants;
import com.example.textile.constants.TextileConstants;
import com.example.textile.entity.Employee;
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
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
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
                                       ModelMap model, RedirectAttributes redirectAttributes, HttpServletRequest request) throws InvalidObjectPopulationException {
        String logPrefix = "submitEmployee() ";
        log.info("{} | Entry",logPrefix);
        ModelAndView modelAndView = new ModelAndView("/employees");
        ActionExecutor actionExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);
        parameterMap.put(TextileConstants.USER, getLoggedInUser());

        EmployeeCommand oldEmployeeBackUp = cloneEmployeeCommand(employee);

        ActionResponse response = new ActionResponse(ResponseType.SUCCESS);
        try {
            response = actionExecutor.execute(employee, parameterMap, result, model);
            if (ResponseType.SUCCESS.equals(response.getResponseType())) {
                logPrefix += "ResponseType=SUCCESS";
                redirectAttributes.addFlashAttribute(CommandConstants.EMPLOYEE_COMMAND,employee);
                redirectAttributes.addFlashAttribute("isRedirect",true);
                redirectAttributes.addFlashAttribute("actionResponse",response);
                redirectAttributes.addFlashAttribute("successMessage",
                        messageSource.getMessage("ActionResponse.Success.Submit.employee",
                                new Object[]{employee.getEmployee().getFullName(),employee.getEmployee().getId()},
                                request.getLocale()));
                modelAndView.setViewName("redirect:/employees");
            } else {
                logPrefix += "ResponseType=Error";
                log.error("{} result validation error", logPrefix);
                result.getAllErrors().forEach(System.out::println);
            }
            log.info("{} | Complete", logPrefix);
        } catch (DataAccessException e) {
            actionExecutor.prePopulateOptionsAndFields(employee, model);
            log.error("DB_Error in saving employee: "+e, e);
            response = new ActionResponse(ResponseType.FAILURE);
            if (e instanceof ObjectOptimisticLockingFailureException) {
                response.addErrorMessage(messageSource.getMessage("System.Exception.Optimistic.employee",new Object[]{employee.getEmployee().getId()},request.getLocale()));
            } else {
                response.addErrorMessage(messageSource.getMessage("System.Exception.DB",null,request.getLocale()));
            }
            model.addAttribute("actionResponse",response);
            actionExecutor.prePopulateOptionsAndFields(oldEmployeeBackUp, model);
            model.addAttribute(CommandConstants.EMPLOYEE_COMMAND,oldEmployeeBackUp);
        } catch (Throwable e) {
            actionExecutor.prePopulateOptionsAndFields(employee, model);
            log.error("SystemError: in saving invoice-"+e.getLocalizedMessage(), e);
            response = new ActionResponse(ResponseType.FAILURE);
            String message = messageSource.getMessage("System.Error",null,request.getLocale());
            response.addErrorMessage(message);
            model.addAttribute("actionResponse",response);
            actionExecutor.prePopulateOptionsAndFields(oldEmployeeBackUp, model);
            model.addAttribute(CommandConstants.EMPLOYEE_COMMAND,oldEmployeeBackUp);
        }

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView viewEmployeeById(@PathVariable("id") Long id,
                                         @ModelAttribute(CommandConstants.EMPLOYEE_COMMAND) EmployeeCommand employee, ModelMap model) {
        ModelAndView modelAndView = new ModelAndView("/employees");
        ActionExecutor actionExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());

        try {
            Employee byId = employeeService.findById(id);
            employee.setEmployee(byId);
            actionExecutor.prePopulateOptionsAndFields(employee, model);
        } catch (InvalidObjectPopulationException e) {
            log.error("Exception in populating employeeCommand "+ e.getLocalizedMessage(),e);
        } catch (Exception e) {
            log.error("Exception in viewEmpById(): "+e.getLocalizedMessage(),e);
        }

        return modelAndView;
    }

    private EmployeeCommand cloneEmployeeCommand(EmployeeCommand employeeCmd) {
        EmployeeCommand employeeCommand = new EmployeeCommand();

        Employee bkUpEmp = new Employee();
        Employee employee = employeeCmd.getEmployee();
        bkUpEmp.setId(employee.getId());
        bkUpEmp.setFirstName(employee.getFirstName());
        bkUpEmp.setLastName(employee.getLastName());
        bkUpEmp.setDesignation(employee.getDesignation());
        bkUpEmp.setSalary(employee.getSalary());
        bkUpEmp.setBankDetails(employee.getBankDetails());
        bkUpEmp.setPanCardNo(employee.getPanCardNo());
        bkUpEmp.setStatus(employee.getStatus());
        bkUpEmp.setAddress(employee.getAddress());

        employeeCommand.setEmployee(bkUpEmp);
        return employeeCommand;
    }

    @DeleteMapping("/deleteById")
    @ResponseBody
    public String deleteById(@PathParam("id") Long id) {

        try {
            employeeService.deleteById(id);
            log.info("EmployeeId:"+id+" Deleted !!");
            return ResponseType.SUCCESS.getActResponse();
        } catch (Exception e) {
            log.error("Exception while deleting employee id:"+id,e);
            return ResponseType.FAILURE.getActResponse();
        }
    }
}
