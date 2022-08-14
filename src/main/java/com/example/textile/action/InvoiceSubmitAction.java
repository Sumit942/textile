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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class InvoiceSubmitAction extends ActionExecutor<Invoice> {

    private final InvoiceService invoiceService;

    public InvoiceSubmitAction(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Override
    protected ActionResponse onSuccess(Invoice invoice, Map<String, Object> parameterMap, ModelAndView model) {
        String logPrefix = "doSuccess() |";
        log.info("{} Entry", logPrefix);
        ActionType action = (ActionType) parameterMap.get(ShreeramTextileConstants.ACTION);

        invoiceService.save(invoice);
        if (ActionType.SUBMIT.equals(action)) {
            log.info("{} [ActionType=SUBMIT]",logPrefix);
            invoice.setInvoiceNo(invoiceService.getLatestInvoiceNo());
        } else {
            log.info("{} [ActionType=SAVE]",logPrefix);
            invoice.setInvoiceNo(ShreeramTextileConstants.FORMAT_SAVE_INVOICE_NO);
        }
        //saving the by getting the invoiceId
        invoiceService.save(invoice);
        ActionResponse actionResponse = new ActionResponse(ResponseType.SUCCESS);
        log.info("{} [Reponse=SUCCESS, invoiceNo={}, totalAmount={}]",logPrefix,invoice.getInvoiceNo(),invoice.getTotalAmountAfterTax());
        log.info("{} Exit", logPrefix);
        return actionResponse;
    }

    @Override
    protected void doPreSaveOperation(Invoice invoice, BindingResult result) {
        String logPrefix = "doPreSaveOperation() |";
        String logSuffix = "";
        log.info("{} Entry",logPrefix);
        if (invoice.getBillToParty().getId() == null) {
            log.info("{} new invoice",logPrefix);
            //check if the newly added gst already exist
            List<Company> bParty = invoiceService.getCompanyByGst(invoice.getBillToParty().getGst());
            logSuffix += "GST="+invoice.getBillToParty().getGst()+";";
            if (bParty != null && !bParty.isEmpty()) {
                logSuffix += "isDuplicate=YES;";
                Company savedParty = bParty.get(0);
                result.rejectValue("billToParty.gst",
                        "invoiceCommand.billToParty.gst.alreadyExist",
                        new Object[]{savedParty.getName()},
                        "Gst Already registered");
            } else {
                logSuffix += "isDuplicate=NO;";
            }

        } else {
            log.info("{} old invoice {}",logPrefix, invoice.getId());
        }

        log.info("{} Exit [{}]", logPrefix, logSuffix);
    }

    @Override
    protected void doValidation(Invoice invoice, Map<String, Object> parameterMap, BindingResult result, ModelAndView model) {
        String logPrefix = "doValidation() |";
        log.info("{} Entry", logPrefix);
        Map<String,String> errMap = new HashMap<>();

        if (invoice.getInvoiceDate() == null)
            errMap.put("totalAmount","NotNull.invoiceCommand.totalAmount");
        if (invoice.getReverseCharge() == null)
            invoice.setReverseCharge("No");
        if (invoice.getTransportMode() == null || invoice.getTransportMode().getMode() == null)
            errMap.put("transportMode","NotNull.invoiceCommand.transportMode");
        if (invoice.getId() == null) {
            if (invoice.getDateOfSupply() == null) {
                errMap.put("dateOfSupply","NotNull.invoiceCommand.dateOfSupply");
            } else if (invoice.getInvoiceDate() == null) {
                errMap.put("invoiceDate","NotNull.invoiceCommand.invoiceDate");
            } else if (invoice.getDateOfSupply().after(invoice.getInvoiceDate())) {
                errMap.put("dateOfSupply","invoiceCommand.dateOfSupplyAfterInvoiceDate");
            }
            //Validating billToParty
            if (invoice.getBillToParty() != null) {
                if (invoice.getBillToParty().getName() == null)
                    errMap.put("billToParty.name","NotNull.invoiceCommand.billToParty.name");
                if (invoice.getBillToParty().getGst() == null)
                    errMap.put("billToParty.gst","NotNull.invoiceCommand.billToParty.gst");
                if (invoice.getBillToParty().getAddress() == null) {
                    errMap.put("billToParty.address","NotNull.invoiceCommand.billToParty.address");
                } else {
                    if (invoice.getBillToParty().getAddress().getAddress() == null)
                        errMap.put("billToParty.address.address","NotNull.invoiceCommand.billToParty.address.address");
                    if (invoice.getBillToParty().getAddress().getPinCode() == null) {
                        errMap.put("billToParty.address.pinCode","NotNull.invoiceCommand.billToParty.address.pinCode");
                    }
                    if (invoice.getBillToParty().getAddress().getState() == null) {
                        errMap.put("billToParty.address.state","NotNull.invoiceCommand.billToParty.address.state");
                    } else {
                        if (invoice.getBillToParty().getAddress().getState().getName() == null) {
                            errMap.put("billToParty.address.state.name","NotNull.invoiceCommand.billToParty.address.state.name");
                        }
                        if (invoice.getBillToParty().getAddress().getState().getCode() == null || invoice.getBillToParty().getAddress().getState().getCode() <= 0) {
                            errMap.put("billToParty.address.state.code","NotNull.invoiceCommand.billToParty.address.state.code");
                        }
                    }
                }
            }
            //Validating shipToParty
            if (invoice.getShipToParty() != null){
                if (invoice.getBillToParty().getName() == null)
                    errMap.put("shipToParty.name","NotNull.invoiceCommand.shipToParty.name");
                if (invoice.getShipToParty().getAddress() == null) {
                    errMap.put("shipToParty.address","NotNull.invoiceCommand.shipToParty.address");
                } else {
                    if (invoice.getShipToParty().getAddress().getAddress() == null)
                        errMap.put("shipToParty.address.address","NotNull.invoiceCommand.shipToParty.address.address");
                    if (invoice.getShipToParty().getAddress().getPinCode() == null)
                        errMap.put("shipToParty.address.pinCode","NotNull.invoiceCommand.shipToParty.address.pinCode");
                    if (invoice.getShipToParty().getAddress().getState() == null) {
                        errMap.put("shipToParty.address.state","NotNull.invoiceCommand.shipToParty.address.state");
                    } else {
                        if (invoice.getShipToParty().getAddress().getState().getName() == null)
                            errMap.put("shipToParty.address.state.name","NotNull.invoiceCommand.shipToParty.address.state.name");
                        if (invoice.getShipToParty().getAddress().getState().getCode() == null || invoice.getShipToParty().getAddress().getState().getCode() <= 0)
                            errMap.put("shipToParty.address.state.code","NotNull.invoiceCommand.shipToParty.address.state.code");
                    }
                }
                if (invoice.getShipToParty().getGst() == null)
                    errMap.put("shipToParty.gst","NotNull.invoiceCommand.shipToParty.gst");
            }
            if (invoice.getSaleType() == null || invoice.getSaleType().getId() == null || invoice.getSaleType().getSaleType() == null) {
                errMap.put("saleType","NotNull.invoiceCommand.saleType");
            }
            //validating product list
            if (invoice.getProduct() == null || invoice.getProduct().isEmpty()) {
                errMap.put("product","NotNull.invoiceCommand.product");
            } else {
                for (int i = 0; i < invoice.getProduct().size(); i++) {
                    ProductDetail prod = invoice.getProduct().get(i);
                    if (prod.getProduct() == null) {
                        errMap.put("product.product","NotNull.invoiceCommand.product.product");
                    } else {

                        if (prod.getProduct().getName() == null)
                            errMap.put("product["+i+"].product.name","NotNull.invoiceCommand.product.product.name");
                        if (prod.getProduct().getHsn() == null)
                            errMap.put("product["+i+"].product.hsn","NotNull.invoiceCommand.product.hsn");
                    }
                    if (prod.getChNo() == null)
                        errMap.put("product["+i+"].chNo","NotNull.invoiceCommand.product.chNo");
                    if (prod.getQuantity() == null || prod.getQuantity() <= 0)
                        errMap.put("product["+i+"].quantity","NotNull.invoiceCommand.product.quantity");
                    if (prod.getRate() == null || prod.getRate() <= 0)
                        errMap.put("product["+i+"].rate","NotNull.invoiceCommand.product.rate");
                    if (prod.getTotalPrice() == null || prod.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0)
                        errMap.put("product["+i+"].totalPrice","NotNull.invoiceCommand.product.totalPrice");
                    if (prod.getUnitOfMeasure() == null || prod.getUnitOfMeasure().getUnitOfMeasure() == null)
                        errMap.put("product["+i+"].unitOfMeasure","NotNull.invoiceCommand.product.unitOfMeasure.unitOfMeasure");
                }
            }
            if (invoice.getPnfCharge() == null || invoice.getPnfCharge().compareTo(BigDecimal.ZERO) < 0)
                errMap.put("pnfCharge","NotNull.invoiceCommand.pnfCharge");
            if (invoice.getTotalAmount() == null || invoice.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0)
                errMap.put("totalAmount","NotNull.invoiceCommand.totalAmount");
            if (invoice.getcGst() == null || invoice.getcGst().compareTo(BigDecimal.ZERO) <= 0)
                errMap.put("cGst","NotNull.invoiceCommand.cGst");
            if (invoice.getsGst() == null || invoice.getsGst().compareTo(BigDecimal.ZERO) <= 0)
                errMap.put("sGst","NotNull.invoiceCommand.sGst");
            if (invoice.getTotalTaxAmount() == null || invoice.getTotalTaxAmount().compareTo(BigDecimal.ZERO) <= 0)
                errMap.put("totalTaxAmount","NotNull.invoiceCommand.totalTaxAmount");
            if (invoice.getRoundOff() == null)
                errMap.put("roundOff","NotNull.invoiceCommand.roundOff");
            if (invoice.getTotalAmountAfterTax() == null || invoice.getTotalAmountAfterTax().compareTo(BigDecimal.ZERO) <= 0)
                errMap.put("totalAmountAfterTax","NotNull.invoiceCommand.totalAmountAfterTax");
            if (invoice.getTotalInvoiceAmountInWords() == null || invoice.getTotalInvoiceAmountInWords().equalsIgnoreCase("Zero"))
                errMap.put("totalInvoiceAmountInWords","NotNull.invoiceCommand.totalInvoiceAmountInWords");
        }

        //adding validation error in Binding result
        errMap.forEach(result::rejectValue);

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
