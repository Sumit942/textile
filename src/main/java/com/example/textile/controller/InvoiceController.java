package com.example.textile.controller;

import com.example.textile.action.InvoiceSubmitAction;
import com.example.textile.command.CommandConstants;
import com.example.textile.entity.Invoice;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.exception.ServiceActionException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.InvoiceService;
import com.example.textile.utility.ShreeramTextileConstants;
import com.example.textile.utility.factory.ActionExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    private Map<String, ActionExecutor> actionExecutorMap;

    @PostConstruct
    public void init() {
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(InvoiceController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new InvoiceSubmitAction(invoiceService));
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
    public ModelAndView showAllInvoices() {
        ModelAndView model = new ModelAndView("/invoiceList");
        List<Invoice> invoices = invoiceService.findAll();
        model.addObject("invoices",invoices);

        return model;
    }

    @GetMapping("/submit")
    public ModelAndView getInvoice(@ModelAttribute(CommandConstants.INVOICE_COMMAND) Invoice invoice,
                                   RedirectAttributes redirectAttributes) {
        log.info("show invoice");
        ModelAndView model = new ModelAndView("/invoice");
        try {
            actionExecutorMap.get(ActionType.SUBMIT.getActionType()).prePopulateOptionsAndFields(invoice, model);
        } catch (InvalidObjectPopulationException e) {
            log.error("Exception: {} prePopulation","getInvoice()",e);
        }
        return model;
    }

    @PostMapping("/submit")
    public ModelAndView saveInvoice(@Valid @ModelAttribute(CommandConstants.INVOICE_COMMAND) Invoice invoice,
                                      BindingResult result, RedirectAttributes redirectAttr) throws ServiceActionException {
        ModelAndView model =  new ModelAndView("/invoice");
        String logPrefix = "saveInvoice() |";
        log.info("{} Entry",logPrefix);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);

        ActionExecutor actExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());

        try {
            ActionResponse response = actExecutor.execute(invoice, parameterMap, result,model);
            if (ResponseType.SUCCESS.equals(response.getResponseType())) {
                model.setViewName("redirect:submit");
                log.info("{} saved Successfully!!", logPrefix);
            } else {
                log.error("result has doValidation Errors");
                result.getAllErrors().forEach(System.out::println);
                log.info("{} save Unsuccessfull", logPrefix);
            }
        } catch (Exception e) {
            result.getAllErrors().forEach(System.out::println);
            log.error("Error in saving invoice", e);
        }
        log.info("{} Exit",logPrefix);
        return model;
    }
}
