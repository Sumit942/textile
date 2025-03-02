package com.example.textile.dto;

import com.example.textile.entity.Address;
import com.example.textile.entity.BankDetail;
import com.example.textile.entity.CompanyType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CompanyDto {
    public CompanyDto(Long id, String name, String gst, String code) {
        this.id = id;
        this.name = name;
        this.gst = gst;
        this.code = code;
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
    private String code;
}
