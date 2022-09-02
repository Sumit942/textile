package com.example.textile.service;

import com.example.textile.entity.Company;

import java.util.List;

public interface CompanyService {

    List<Company> findByNameLike(String name);

    Company save(Company billToParty);
}
