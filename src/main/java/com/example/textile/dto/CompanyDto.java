package com.example.textile.dto;

import com.example.textile.entity.Address;
import com.example.textile.entity.BankDetail;
import com.example.textile.entity.CompanyType;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CompanyDto {
    private Long id;
    private String name;
    private Address address;
    private Address ofcAddress;
    private String gst;
    private String emailId;
    private String mobileNo;
    private List<BankDetail> bankDetails;
    private Date insert_dt;
    private Date update_dt;
    private CompanyType companyType;
}
