package com.example.textile.service;

import com.example.textile.dto.CompanyDto;
import com.example.textile.entity.Company;

import java.util.List;

public interface CompanyService {

    List<Company> findByNameLike(String name);

    Company save(Company billToParty);

    List<Company> findAll();

    Company findById(Long id);

    CompanyDto save(CompanyDto companyDto);

    List<CompanyDto> getIdNameAndGstByName(String name);
}
