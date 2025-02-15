package com.example.textile.dto;

import com.example.textile.entity.Address;
import com.example.textile.entity.BankDetail;
import com.example.textile.entity.CompanyType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class CompanyDto {
    public CompanyDto(Long id, String name, String gst) {
        this.id = id;
        this.name = name;
        this.gst = gst;
    }
    private Long id;
    private String name;
    private Address address;
    private Address ofcAddress;
    private String gst;
    private String emailId;
    private String mobileNo;
    private List<BankDetail> bankDetails;
    private CompanyType companyType;
}
