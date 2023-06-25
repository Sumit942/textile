package com.example.textile.controller;

import com.example.textile.action.ChallanSubmitAction;
import com.example.textile.command.ChallanCommand;
import com.example.textile.constants.CommandConstants;
import com.example.textile.constants.TextileConstants;
import com.example.textile.entity.Challan;
import com.example.textile.entity.Invoice;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.exception.ServiceActionException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.ChallanService;
import com.example.textile.utility.ShreeramTextileConstants;
import com.example.textile.utility.factory.ActionExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/challan")
public class ChallanController extends BaseController{

    @Autowired
    private ChallanService challanService;

    private Map<String, ActionExecutor> actionExecutorMap;

    @PostConstruct
    public void init() {
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(ChallanController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new ChallanSubmitAction(challanService));
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(dateFormat,true);
        dataBinder.registerCustomEditor(Date.class, dateEditor);
    }

    @GetMapping("/viewAll")
    public ModelAndView findAll(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                @RequestParam(required = false, defaultValue = "challanNo") String fieldName,
                                ModelMap model) {
        log.info("findAll() | Entry");
        ModelAndView modelAndView = new ModelAndView("/challanList");

        Page<Challan> challanList = challanService.findAllByPageNumberAndPageSizeOrderByField(pageNumber, pageSize, fieldName);
        modelAndView.addObject("challans",challanList);

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getChallanById(@PathVariable("id") Long id, ModelMap model) {
        log.info("getInvoiceById() Entry");
        ModelAndView modelView = new ModelAndView("/challan");
        try {
            Challan challan = challanService.findById(id);
            actionExecutorMap.get(ActionType.SUBMIT.getActionType()).prePopulateOptionsAndFields(challan, model);
            model.addAttribute(CommandConstants.CHALLAN_COMMAND,challan);
        } catch (Throwable e) {
            log.error("Exception: {} prePopulation","getChallanById()",e);
        }

        return modelView;
    }

    @GetMapping
    public ModelAndView saveChallanGet(@ModelAttribute(CommandConstants.CHALLAN_COMMAND) ChallanCommand challan,
                                       BindingResult result, ModelMap model, RedirectAttributes redirectAttributes,
                                       HttpServletRequest request) throws InvalidObjectPopulationException {
        ModelAndView modelAndView =  new ModelAndView("challan");
        actionExecutorMap.get(ActionType.SUBMIT.getActionType()).prePopulateOptionsAndFields(challan, model);

        return modelAndView;
    }

    @PostMapping
    public ModelAndView saveChallan(@Valid @ModelAttribute(CommandConstants.CHALLAN_COMMAND) ChallanCommand command,
                                   BindingResult result, ModelMap model, RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) throws ServiceActionException {
        String logPrefix = "saveChallan() |";
        log.info("{} Entry -> {}",logPrefix, command);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);
        parameterMap.put(TextileConstants.USER,getLoggedInUser());

        ActionExecutor actExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());
        ActionResponse response;
        ModelAndView modelAndView =  new ModelAndView("challan");
        try {
            response = actExecutor.execute(command, parameterMap, result,model);
            if (ResponseType.SUCCESS.equals(response.getResponseType())) {
                modelAndView.setViewName("redirect:challan/viewAll");
            } else {
                log.error("result has doValidation Errors - [" + result.getAllErrors() + "]");
                log.info("{} save Unsuccessfull", logPrefix);
            }
        }catch (DataAccessException e) {
            log.error("DB_Error: in saving challan command: ",e);
            response = new ActionResponse(ResponseType.FAILURE);
            if (e instanceof ObjectOptimisticLockingFailureException) {
                response.addErrorMessage(messageSource.getMessage("System.Exception.Optimistic",new Object[]{command.getChallan().getChallanNo(),command.getChallan().getId()},request.getLocale()));
            } else {
                response.addErrorMessage(messageSource.getMessage("System.Exception.DB",null,request.getLocale()));
            }
            model.addAttribute("actionResponse",response);
            //TODO: add oldChallan backkup
            actExecutor.prePopulateOptionsAndFields(command, model);
            model.addAttribute(CommandConstants.CHALLAN_COMMAND,command);
        } catch (Throwable e) {
            log.error("SystemError: in saving challan", e);
            response = new ActionResponse(ResponseType.FAILURE);
            String message = messageSource.getMessage("System.Error",null,request.getLocale());
            response.addErrorMessage(message);
            model.addAttribute("actionResponse",response);
            //TODO: add oldChallan backkup
            actExecutor.prePopulateOptionsAndFields(command, model);
            model.addAttribute(CommandConstants.CHALLAN_COMMAND,command);
        }

        return modelAndView;
    }
}
