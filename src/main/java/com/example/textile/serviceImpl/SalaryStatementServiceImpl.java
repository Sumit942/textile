package com.example.textile.serviceImpl;

import com.example.textile.entity.Employee;
import com.example.textile.entity.SalaryStatement;
import com.example.textile.repo.EmployeeRepository;
import com.example.textile.repo.SalaryStatementRepository;
import com.example.textile.service.SalaryStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public class SalaryStatementServiceImpl implements SalaryStatementService {

    @Autowired
    EmployeeRepository employeeRepo;

    @Autowired
    SalaryStatementRepository statementRepo;

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public List<SalaryStatement> findAllOrderByAndLimit(String fieldName, int pageNumber, int pageSize) {
        return findAllByPageNumberAndPageSizeOrderByField(fieldName, pageNumber, pageSize).getContent();
    }

    public Page<SalaryStatement> findAllByPageNumberAndPageSizeOrderByField(String fieldName, int pageNumber, int pageSize) {
        return statementRepo.findAll(PageRequest.of(pageNumber, pageSize).withSort(Sort.by(fieldName).descending()));
    }

    @Override
    public List<SalaryStatement> saveAll(List<SalaryStatement> statements) {
        return statementRepo.saveAll(statements);
    }
}
