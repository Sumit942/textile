package com.example.textile.utility;

import com.example.textile.entity.BankDetail;
import com.example.textile.entity.Company;
import com.example.textile.entity.InvoiceView;
import com.example.textile.utility.factory.Factory;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class ShreeramTextile implements Factory {
    private static ShreeramTextile shreeramTextile = null;

    private static final SimpleDateFormat DATE_FORMAT_ddMMYYYY = new SimpleDateFormat(ShreeramTextileConstants.DATE_FORMAT_ddMMYYYY);
    ShreeramTextile() {
        log.debug("Initialized");
        country.setName(ShreeramTextileConstants.COUNTRY);
        state.setCountry(country);
        address.setState(state);
        company.setAddress(address);
        company.setBankDetails(bankDetails);

        state.setName(ShreeramTextileConstants.STATE);
        state.setCode(ShreeramTextileConstants.STATE_CODE);

        address.setPinCode(ShreeramTextileConstants.PINCODE);
        address.setAddress(ShreeramTextileConstants.ADDRESS);

        company.setEmailId(ShreeramTextileConstants.EMAIL);
        company.setMobileNo(ShreeramTextileConstants.MOBILE_NOS);
        company.setGst(ShreeramTextileConstants.GST);
        company.setName(ShreeramTextileConstants.NAME);

        BankDetail bankDetail = new BankDetail();
        bankDetail.setBankName(ShreeramTextileConstants.BANK_NAME);
        bankDetail.setAccountNo(ShreeramTextileConstants.BANK_AC_NO);
        bankDetail.setIfsc(ShreeramTextileConstants.BANK_IFSC);
        bankDetail.setBranch(ShreeramTextileConstants.BANK_BRANCH);
        bankDetails.add(bankDetail);
    }

    public static ShreeramTextile getInstance() {
        if (shreeramTextile == null)
            synchronized (ShreeramTextile.class) {
                if (shreeramTextile == null)
                    shreeramTextile = new ShreeramTextile();
            }

        return shreeramTextile;
    }

    public static ShreeramTextile getInfo() {
        return getInstance();
    }

    public Company getCompanyDetails() {
        return company;
    }

    /**
     * @param valueMapList List of rows to be prepared
     * @param domainList Domain Object
     *
     * Adds and Total Amount row at last position to be shown in Excel/Pdf etc..
     */
    public static void addTotalAmountRow(List<Map<String, Object>> valueMapList, List domainList) {
        log.info("addTotalAmountRow() Entry");
        Map<String, Object> valueMapTotal = new HashMap<>();

        //add an empty row before total amount row
        valueMapList.add(valueMapTotal);

        valueMapTotal = new HashMap<>();

        List<InvoiceView> list = (List<InvoiceView>) domainList;
        BigDecimal amount = list.stream().map(InvoiceView::getTotalAmount)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tax = list.stream().map(InvoiceView::getTotalTaxAmount)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal pnf = list.stream().map(InvoiceView::getPnfCharge)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmount = list.stream().map(InvoiceView::getTotalAmountAfterTax)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal paidAmount = list.stream().map(InvoiceView::getPaidAmount)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal debit = list.stream().map(InvoiceView::getAmtDr)
                .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

        valueMapTotal.put("Party Name","Total:"); //assuming to add total in party name column
        valueMapTotal.put("Amount",amount);
        valueMapTotal.put("Total Tax",tax);
        valueMapTotal.put("PnF",pnf);
        valueMapTotal.put("Total Amount",totalAmount);
        valueMapTotal.put("Paid",paidAmount);
        valueMapTotal.put("Debit",debit);
        valueMapList.add(valueMapTotal);
    }



    public static String getStringCellValueForBool(Boolean value, String header) {
        if (header.contains("Payment")) {
            return value ? "Paid" : "UnPaid";
        } else {
            return value ? "Yes" : "No";
        }
    }

    public static SimpleDateFormat getDateFormat(String dateFormat) {

        if (ShreeramTextileConstants.DATE_FORMAT_ddMMYYYY.equals(dateFormat))
            return DATE_FORMAT_ddMMYYYY;

        return DATE_FORMAT_ddMMYYYY;
    }
}
