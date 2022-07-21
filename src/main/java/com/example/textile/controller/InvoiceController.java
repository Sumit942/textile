package com.example.textile.controller;

import com.example.textile.action.InvoiceSubmitAction;
import com.example.textile.command.CommandConstants;
import com.example.textile.entity.Invoice;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.executors.ActionExecutors;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.InvoiceService;
import com.example.textile.utility.ShreeramTextileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/")
    public ModelAndView showAllInvoices() {
        ModelAndView model = new ModelAndView("/invoices");
        List<Invoice> invoices = invoiceService.findAll();
        model.addObject("invoices",invoices);

        return model;
    }

    @GetMapping("/save")
    public ModelAndView getInvoice(@ModelAttribute(CommandConstants.INVOICE_COMMAND) Invoice invoice) {
        log.info("show invoice");
        return new ModelAndView("/invoice");
    }

    @PostMapping("/save")
    public ModelAndView saveInvoice(@Valid @ModelAttribute(CommandConstants.INVOICE_COMMAND) Invoice invoice,
                                      BindingResult result, RedirectAttributes redirectAttr) {

        String logPrefix = "saveInvoice() |";
        System.out.println(logPrefix + "\n" + invoice);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SAVE);

        ActionExecutors actExecutor = new InvoiceSubmitAction(invoiceService);

        try {
            ActionResponse response = actExecutor.execute(invoice,parameterMap,result);
            if (ResponseType.SUCCESS.equals(response.getResponseType())) {
                log.info("{} saved Successfully!!", logPrefix);
            } else {
                log.info("{} save Unsuccessfull", logPrefix);
            }
        } catch (Exception e) {
            log.error("Error in saving invoice",e);
        }
        return new ModelAndView("redirect:save");
    }
}
