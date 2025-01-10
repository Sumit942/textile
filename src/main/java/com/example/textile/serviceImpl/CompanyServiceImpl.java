package com.example.textile.serviceImpl;

import com.example.textile.dto.CompanyDto;
import com.example.textile.entity.Company;
import com.example.textile.exception.CompanyNotFoundException;
import com.example.textile.repo.CompanyRepository;
import com.example.textile.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepo;
    private final ModelMapper modelMapper;

    public CompanyServiceImpl(CompanyRepository companyRepo, ModelMapper modelMapper) {
        this.companyRepo = companyRepo;
        this.modelMapper = modelMapper;
    }

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

    @Override
    public CompanyDto save(CompanyDto companyDto) {
        log.info("save() dto: " + companyDto);
        Company company = modelMapper.map(companyDto, Company.class);
        company = this.companyRepo.save(company);

        return modelMapper.map(company, CompanyDto.class);
    }
}
