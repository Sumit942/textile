package com.example.textile;

import com.example.textile.action.InvoiceSubmitAction;
import com.example.textile.entity.*;
import com.example.textile.enums.ActionType;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.service.InvoiceService;
import com.example.textile.utility.ShreeramTextileConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.RestTemplate;

import java.beans.PropertyEditor;
import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
class TextileApplicationTests {

    @Autowired
    InvoiceService invoiceService;

//    @Test
    void testSaveInvoiceMapping() {
        System.out.println("SUMEET running checkSaveInvoiceService test1...");
        Country country = new Country();
//        country.setName("INDIA");
        State state = new State();
//        state.setName("Maharashtra");
//        state.setCode(27);
        state.setCountry(country);
        Address address = new Address();
//        address.setAddress("240  INDL ESTATE, MARG, PAREL WEST, MUMBAI");
//        address.setPinCode(400013);
        address.setState(state);
        BankDetail bankDetail = new BankDetail();
//        bankDetail.setBankName("IDFC Bank");
//        bankDetail.setBranch("New Thane");
//        bankDetail.setAccountNo("***1239");
//        bankDetail.setIfsc("IDFC000005");
        List<BankDetail> bankDetails = new ArrayList<>();
        bankDetails.add(bankDetail);
        Company company = new Company();
        company.setAddress(address);
        company.setBankDetails(bankDetails);
//        company.setName("C SQUARE CLOTHING");
//        company.setGst("27AAYFA0586B1ZB");
//        company.setId(4L);

        Invoice invoice = new Invoice();
        invoice.setBillToParty(company);
//        invoice.setInvoiceDate(new Date());
//        invoice.setTotalAmount(BigDecimal.valueOf(100000.00));
//        invoice.setsGst(BigDecimal.valueOf(2500));
//        invoice.setcGst(BigDecimal.valueOf(2500));
//        invoice.setTotalTaxAmount(BigDecimal.valueOf(105000.00));
//        invoice.setTotalAmountAfterTax(BigDecimal.valueOf(105000.00));
//        invoice.setTotalInvoiceAmountInWords("One Lakh Five Thousand Only.");
//        invoice.setPlaceOfSupply("Bhiwandi");

        invoiceService.save(invoice);
        System.out.println(invoice);
        System.out.println("save succesfull!!");
    }


//    @Test
    void checkSaveInvoiceService() {

        System.out.println("SUMEET running checkSaveInvoiceService test...");
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(new Date());
        invoice.setTotalAmount(BigDecimal.valueOf(100000.00));
        invoice.setsGst(BigDecimal.valueOf(2500));
        invoice.setcGst(BigDecimal.valueOf(2500));
        invoice.setTotalTaxAmount(BigDecimal.valueOf(105000.00));
        invoice.setTotalAmountAfterTax(BigDecimal.valueOf(105000.00));
        invoice.setTotalInvoiceAmountInWords("One Lakh Five Thousand Only.");
        invoice.setPlaceOfSupply("Bhiwandi");

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);

        BindingResult result = new BindingResult() {
            @Override
            public Object getTarget() {
                return null;
            }

            @Override
            public Map<String, Object> getModel() {
                return null;
            }

            @Override
            public Object getRawFieldValue(String field) {
                return null;
            }

            @Override
            public PropertyEditor findEditor(String field, Class<?> valueType) {
                return null;
            }

            @Override
            public PropertyEditorRegistry getPropertyEditorRegistry() {
                return null;
            }

            @Override
            public String[] resolveMessageCodes(String errorCode) {
                return new String[0];
            }

            @Override
            public String[] resolveMessageCodes(String errorCode, String field) {
                return new String[0];
            }

            @Override
            public void addError(ObjectError error) {

            }

            @Override
            public String getObjectName() {
                return null;
            }

            @Override
            public String getNestedPath() {
                return null;
            }

            @Override
            public void setNestedPath(String nestedPath) {

            }

            @Override
            public void pushNestedPath(String subPath) {

            }

            @Override
            public void popNestedPath() throws IllegalStateException {

            }

            @Override
            public void reject(String errorCode) {

            }

            @Override
            public void reject(String errorCode, String defaultMessage) {

            }

            @Override
            public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {

            }

            @Override
            public void rejectValue(String field, String errorCode) {

            }

            @Override
            public void rejectValue(String field, String errorCode, String defaultMessage) {

            }

            @Override
            public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) {

            }

            @Override
            public void addAllErrors(Errors errors) {

            }

            @Override
            public boolean hasErrors() {
                return false;
            }

            @Override
            public int getErrorCount() {
                return 0;
            }

            @Override
            public List<ObjectError> getAllErrors() {
                return null;
            }

            @Override
            public boolean hasGlobalErrors() {
                return false;
            }

            @Override
            public int getGlobalErrorCount() {
                return 0;
            }

            @Override
            public List<ObjectError> getGlobalErrors() {
                return null;
            }

            @Override
            public ObjectError getGlobalError() {
                return null;
            }

            @Override
            public boolean hasFieldErrors() {
                return false;
            }

            @Override
            public int getFieldErrorCount() {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors() {
                return null;
            }

            @Override
            public FieldError getFieldError() {
                return null;
            }

            @Override
            public boolean hasFieldErrors(String field) {
                return false;
            }

            @Override
            public int getFieldErrorCount(String field) {
                return 0;
            }

            @Override
            public List<FieldError> getFieldErrors(String field) {
                return null;
            }

            @Override
            public FieldError getFieldError(String field) {
                return null;
            }

            @Override
            public Object getFieldValue(String field) {
                return null;
            }

            @Override
            public Class<?> getFieldType(String field) {
                return null;
            }
        };

        ActionExecutor<Invoice> actionExecutor = new InvoiceSubmitAction(invoiceService);

        actionExecutor.execute(invoice, parameterMap, result);
        System.out.println("Test run completed!!");
    }

//    @Test
    void contextLoads() {
        String url = "http://localhost:8080/textile/invoices/save";

        System.out.println("SUMEET running test1...");
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(new Date());
        invoice.setTotalAmount(BigDecimal.valueOf(100000.00));
        invoice.setsGst(BigDecimal.valueOf(2500));
        invoice.setcGst(BigDecimal.valueOf(2500));
        invoice.setTotalTaxAmount(BigDecimal.valueOf(105000.00));
        invoice.setTotalAmountAfterTax(BigDecimal.valueOf(105000.00));
        invoice.setTotalInvoiceAmountInWords("One Lakh Five Thousand Only.");
        invoice.setPlaceOfSupply("Bhiwandi");

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Invoice> request = new HttpEntity<>(invoice);


        ResponseEntity<Invoice> invoiceRes = restTemplate.postForEntity(url, request, Invoice.class);

        System.out.println(invoiceRes);

    }

}
