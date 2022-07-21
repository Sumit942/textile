package com.example.textile.action;

import com.example.textile.entity.Invoice;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.executors.ActionExecutors;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.InvoiceService;
import com.example.textile.utility.ShreeramTextileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

import java.util.Map;

@Slf4j
public class InvoiceSubmitAction extends ActionExecutors<Invoice> {

    private final InvoiceService invoiceService;

    public InvoiceSubmitAction(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    protected ActionResponse onSuccess(Invoice invoice, Map<String, Object> parameterMap) {
        String logPrefix = "doSuccess() |";
        log.info("{} Entry", logPrefix);
        ActionType action = (ActionType) parameterMap.get(ShreeramTextileConstants.ACTION);
        if (ActionType.SUBMIT.equals(action)) {
            log.info("{} [ActionType=SUBMIT]",logPrefix);
            invoice.setInvoiceNo(invoiceService.getLatestInvoiceNo());
        } else {
            log.info("{} [ActionType=SAVE]",logPrefix);
            invoice.setInvoiceNo(ShreeramTextileConstants.FORMAT_SAVE_INVOICE_NO);
        }
        invoiceService.save(invoice);
        ActionResponse actionResponse = new ActionResponse(ResponseType.SUCCESS);
        log.info("{} [Reponse=SUCCESS, invoiceNo={}, totalAmount={}]",logPrefix,invoice.getInvoiceNo(),invoice.getTotalAmountAfterTax());
        log.info("{} Exit", logPrefix);
        return actionResponse;
    }

    @Override
    protected void doValidation(Invoice invoice, Map<String, Object> parameterMap, BindingResult result) {
        String logPrefix = "doValidation() |";
        log.info("{} Entry", logPrefix);

        log.info("{} Exit", logPrefix);
    }
}
