package com.example.textile.utility;

import com.example.textile.entity.BankDetail;
import com.example.textile.entity.Company;
import com.example.textile.utility.factory.Factory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShreeramTextile implements Factory {
    private static final ShreeramTextile shreeramTextile = new ShreeramTextile();

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

    public static ShreeramTextile getInfo() {
        return shreeramTextile;
    }

    public Company getCompanyDetails() {
        return company;
    }
}
