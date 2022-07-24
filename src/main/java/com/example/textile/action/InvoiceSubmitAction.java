package com.example.textile.action;

import com.example.textile.entity.*;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.InvoiceService;
import com.example.textile.utility.ShreeramTextileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
public class InvoiceSubmitAction extends ActionExecutor<Invoice> {

    private final InvoiceService invoiceService;

    public InvoiceSubmitAction(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    protected ActionResponse onSuccess(Invoice invoice, Map<String, Object> parameterMap) {
        String logPrefix = "doSuccess() |";
        log.info("{} Entry", logPrefix);
        ActionType action = (ActionType) parameterMap.get(ShreeramTextileConstants.ACTION);
        doPreSaveOperation(invoice);
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

    private void doPreSaveOperation(Invoice invoice) {

        if (invoice.getId() == null) {
            if (invoice.getUser() == null) {

            }
        }
    }

    @Override
    protected void doValidation(Invoice invoice, Map<String, Object> parameterMap, BindingResult result) {
        String logPrefix = "doValidation() |";
        log.info("{} Entry", logPrefix);
        Set<String> errMsg = new HashSet<>();

        if (invoice.getInvoiceDate() == null)
            errMsg.add("NotNull.invoiceCommand.totalAmount");
        if (invoice.getReverseCharge() == null)
            invoice.setReverseCharge("No");
        if (invoice.getTransportMode() == null || invoice.getTransportMode().getMode() == null)
            errMsg.add("NotNull.invoiceCommand.transportMode");
        if (invoice.getId() == null) {
            if (invoice.getDateOfSupply() == null) {
                errMsg.add("NotNull.invoiceCommand.dateOfSupply");
            } else if (invoice.getInvoiceDate() != null) {
                errMsg.add("NotNull.invoiceCommand.invoiceDate");
            } else if (invoice.getDateOfSupply().after(invoice.getInvoiceDate())) {
                errMsg.add("invoiceCommand.dateOfSupplyAfterInvoiceDate");
            }
            //Validating billToParty
            if (invoice.getBillToParty() != null) {
                if (invoice.getBillToParty().getName() == null)
                    errMsg.add("NotNull.invoiceCommand.billToParty.name");
                if (invoice.getBillToParty().getGst() == null)
                    errMsg.add("NotNull.invoiceCommand.billToParty.gst");
                if (invoice.getBillToParty().getAddress() == null) {
                    errMsg.add("NotNull.invoiceCommand.billToParty.address");
                } else {
                    if (invoice.getBillToParty().getAddress().getAddress() == null)
                        errMsg.add("NotNull.invoiceCommand.billToParty.address.address");
                    if (invoice.getBillToParty().getAddress().getPinCode() == null) {
                        errMsg.add("NotNull.invoiceCommand.billToParty.address.pinCode");
                    }
                    if (invoice.getBillToParty().getAddress().getState() == null) {
                        errMsg.add("NotNull.invoiceCommand.billToParty.address.state");
                    } else {
                        if (invoice.getBillToParty().getAddress().getState().getName() == null) {
                            errMsg.add("NotNull.invoiceCommand.billToParty.address.state.name");
                        }
                        if (invoice.getBillToParty().getAddress().getState().getCode() == null || invoice.getBillToParty().getAddress().getState().getCode() <= 0) {
                            errMsg.add("NotNull.invoiceCommand.billToParty.address.state.code");
                        }
                    }
                }
            }
            //Validating shipToParty
            if (invoice.getShipToParty() != null){
                if (invoice.getShipToParty().getAddress() == null) {
                    errMsg.add("NotNull.invoiceCommand.shipToParty.address");
                } else {
                    if (invoice.getShipToParty().getAddress().getAddress() == null)
                        errMsg.add("NotNull.invoiceCommand.shipToParty.address.address");
                    if (invoice.getShipToParty().getAddress().getPinCode() == null)
                        errMsg.add("NotNull.invoiceCommand.shipToParty.address.pincode");
                    if (invoice.getShipToParty().getAddress().getState() == null) {
                        errMsg.add("NotNull.invoiceCommand.shipToParty.address.state");
                    } else {
                        if (invoice.getShipToParty().getAddress().getState().getName() == null)
                            errMsg.add("NotNull.invoiceCommand.shipToParty.address.state.name");
                        if (invoice.getShipToParty().getAddress().getState().getCode() == null || invoice.getShipToParty().getAddress().getState().getCode() <= 0)
                            errMsg.add("NotNull.invoiceCommand.shipToParty.address.state.code");
                    }
                }
                if (invoice.getShipToParty().getGst() == null)
                    errMsg.add("NotNull.invoiceCommand.shipToParty.gst");
            }
            if (invoice.getSaleType().getId() == null || invoice.getSaleType().getSaleType() == null) {
                errMsg.add("NotNull.invoiceCommand.saleType");
            }
            //validating product list
            if (invoice.getProduct() == null || invoice.getProduct().isEmpty()) {
                errMsg.add("NotNull.invoiceCommand.product");
            } else {
                for (ProductDetail prod: invoice.getProduct()) {

                    if (prod.getProduct() == null) {
                        errMsg.add("NotNull.invoiceCommand.product.product");
                    } else {

                        if (prod.getProduct().getName() == null)
                            errMsg.add("NotNull.invoiceCommand.product.product.name");
                        if (prod.getProduct().getHsn() == null)
                            errMsg.add("NotNull.invoiceCommand.product.product.hsn");
                    }
                    if (prod.getChNo() == null)
                        errMsg.add("NotNull.invoiceCommand.product.product.chNo");
                    if (prod.getQuantity() == null || prod.getQuantity() <= 0)
                        errMsg.add("NotNull.invoiceCommand.product.product.quantity");
                    if (prod.getRate() == null || prod.getRate() <= 0)
                        errMsg.add("NotNull.invoiceCommand.product.product.rate");
                    if (prod.getTotalPrice() == null || prod.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0)
                        errMsg.add("NotNull.invoiceCommand.product.product.totalPrice");
                    if (prod.getUnitOfMeasure() == null || prod.getUnitOfMeasure().getUnitOfMeasure() == null)
                        errMsg.add("NotNull.invoiceCommand.product.product.unitOfMeasure");
                }
            }
            if (invoice.getPnfCharge() == null || invoice.getPnfCharge().compareTo(BigDecimal.ZERO) < 0)
                errMsg.add("NotNull.invoiceCommand.pnfCharge");
            if (invoice.getTotalAmount() == null || invoice.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0)
                errMsg.add("NotNull.invoiceCommand.totalAmount");
            if (invoice.getcGst() == null || invoice.getcGst().compareTo(BigDecimal.ZERO) <= 0)
                errMsg.add("NotNull.invoiceCommand.cGst");
            if (invoice.getsGst() == null || invoice.getsGst().compareTo(BigDecimal.ZERO) <= 0)
                errMsg.add("NotNull.invoiceCommand.sGst");
            if (invoice.getTotalTaxAmount() == null || invoice.getTotalTaxAmount().compareTo(BigDecimal.ZERO) <= 0)
                errMsg.add("NotNull.invoiceCommand.totalTaxAmount");
            if (invoice.getRoundOff() == null)
                errMsg.add("NotNull.invoiceCommand.roundOff");
            if (invoice.getTotalAmountAfterTax() == null || invoice.getTotalAmountAfterTax().compareTo(BigDecimal.ZERO) <= 0)
                errMsg.add("NotNull.invoiceCommand.totalAmountAfterTax");
            if (invoice.getTotalInvoiceAmountInWords() == null || invoice.getTotalInvoiceAmountInWords().equalsIgnoreCase("Zero"))
                errMsg.add("NotNull.invoiceCommand.totalInvoiceAmountInWords");
        }

        //adding validation error in Binding result
        errMsg.forEach(result::reject);

        log.info("{} Exit", logPrefix);
    }

    @Override
    public void prePopulateOptionsAndFields(Invoice invoice, Object model) throws InvalidObjectPopulationException {
        String logPrefix = "populateOptionsAndFields() |";
        log.info("{} Entry", logPrefix);
        if(!(model instanceof ModelAndView)) {
            throw new InvalidObjectPopulationException(invoice,model);
        }
        ModelAndView modelView = (ModelAndView) model;

        List<TransportMode> transportModes = invoiceService.getTransportModes();
        List<State> states = invoiceService.getStates();
        List<SaleType> saleTypes = invoiceService.getSaleTypes();
        List<Unit> unitOfMeasures = invoiceService.getUnitOfMeasure();

        modelView.addObject("transportModes", transportModes);
        modelView.addObject("states", states);
        modelView.addObject("saleTypes", saleTypes);
        modelView.addObject("unitOfMeasures", unitOfMeasures);

        log.info("{} Exit", logPrefix);

    }
}
