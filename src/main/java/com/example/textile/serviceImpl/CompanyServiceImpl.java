package com.example.textile.serviceImpl;

import com.example.textile.entity.Company;
import com.example.textile.exception.CompanyNotFoundException;
import com.example.textile.repo.CompanyRepository;
import com.example.textile.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepo;

    @Override
    public List<Company> findByNameLike(String name) {
        return this.companyRepo.findByNameLike(name);
    }

    @Override
    public Company save(Company company) {
        log.info("save() | "+company);
        return this.companyRepo.save(company);
    }

    @Override
    public List<Company> findAll() {
        return this.companyRepo.findAll();
    }

    @Override
    public Company findById(Long id) {
        return this.companyRepo.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
    }
}
