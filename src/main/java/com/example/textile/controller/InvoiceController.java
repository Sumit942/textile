package com.example.textile.controller;

import com.example.textile.action.InvoiceSubmitAction;
import com.example.textile.command.CommandConstants;
import com.example.textile.entity.Company;
import com.example.textile.entity.Invoice;
import com.example.textile.entity.State;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.exception.ServiceActionException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.CompanyService;
import com.example.textile.service.InvoiceService;
import com.example.textile.utility.ShreeramTextileConstants;
import com.example.textile.utility.factory.ActionExecutorFactory;
import com.sun.org.apache.xpath.internal.operations.Mod;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/invoices")
public class InvoiceController extends BaseController {

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

    @GetMapping("/invoice/{id}")
    public ModelAndView getInvoiceById(@PathVariable("id") Long id,ModelMap model) {
        log.info("show invoice");
        ModelAndView modelView = new ModelAndView("/invoice");
        try {
            Invoice invoice = invoiceService.finById(id);
            actionExecutorMap.get(ActionType.SUBMIT.getActionType()).prePopulateOptionsAndFields(invoice, model);
            model.addAttribute(CommandConstants.INVOICE_COMMAND,invoice);
        } catch (Throwable e) {
            log.error("Exception: {} prePopulation","getInvoiceById()",e);
        }
        return modelView;
    }

    @GetMapping("/submit")
    public ModelAndView getInvoice(@ModelAttribute(CommandConstants.INVOICE_COMMAND) Invoice invoice,
                                   ModelMap model, RedirectAttributes redirectAttributes) {
        log.info("show invoice");
        ModelAndView modelView = new ModelAndView("invoice");
        try {
            actionExecutorMap.get(ActionType.SUBMIT.getActionType()).prePopulateOptionsAndFields(invoice, model);
        } catch (InvalidObjectPopulationException e) {
            log.error("Exception: {} prePopulation","getInvoice()",e);
        }
        return modelView;
    }

    @PostMapping("/submit")
    public ModelAndView saveInvoice(@Valid @ModelAttribute(CommandConstants.INVOICE_COMMAND) Invoice invoice,
                                    BindingResult result, HttpServletRequest request,
                                    ModelMap model, RedirectAttributes redirectAttr) throws ServiceActionException {
        ModelAndView modelAndView =  new ModelAndView("invoice");
        String logPrefix = "saveInvoice() |";
        log.info("{} Entry -> {}",logPrefix, invoice);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);

        ActionExecutor actExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());
        ActionResponse response;
        try {
            response = actExecutor.execute(invoice, parameterMap, result,model);
            if (ResponseType.SUCCESS.equals(response.getResponseType())) {
                modelAndView.setViewName("redirect:submit");
                log.info("{} saved Successfully!!", logPrefix);
                redirectAttr.addFlashAttribute(CommandConstants.INVOICE_COMMAND,invoice);
                redirectAttr.addFlashAttribute("printInvoice",true);
                redirectAttr.addFlashAttribute("actionResponse",response);
            } else {
                log.error("result has doValidation Errors");
                result.getAllErrors().forEach(System.out::println);
                log.info("{} save Unsuccessfull", logPrefix);
            }
        } catch (DataAccessException e) {
            log.error("DB_Error: in saving invoice: ",e);
            response = new ActionResponse(ResponseType.FAILURE);
            if (e instanceof ObjectOptimisticLockingFailureException) {
                response.addErrorMessage(messageSource.getMessage("System.Exception.Optimistic",new Object[]{invoice.getInvoiceNo(),invoice.getId()},request.getLocale()));
            } else {
                response.addErrorMessage(messageSource.getMessage("System.Exception.DB",null,request.getLocale()));
            }
            model.addAttribute("actionResponse",response);
        } catch (Throwable e) {
            log.error("SystemError: in saving invoice", e);
            response = new ActionResponse(ResponseType.FAILURE);
            String message = messageSource.getMessage("System.Error",null,request.getLocale());
            response.addErrorMessage(message);
            model.addAttribute("actionResponse",response);
        }
        log.info("{} Exit",logPrefix);
        return modelAndView;
    }
    @GetMapping("/bankInvoice")
    public @ResponseBody Invoice showNewInvoice() {
        return new Invoice();
    }
    @GetMapping("/blankCompany")
    public @ResponseBody Company showNewCompany() {
        return new Company();
    }
    @GetMapping("/bankState")
    public @ResponseBody State showState() {
        return new State();
    }

    @GetMapping("/print/{id}")
    public ModelAndView showPrintInvoice(@PathVariable("id") Long id) {
        log.info("showPrintInvoice() | id-"+id);
        ModelAndView modelAndView = new ModelAndView("emailTemplates/SRTI_Invoice.html");
        Invoice invoice = invoiceService.finById(id);
        log.info("showPrintInvoice() | {}",invoice);
        modelAndView.addObject("invoice",invoice);
        return modelAndView;
    }
}
