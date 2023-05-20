package com.example.textile.serviceImpl;

import com.example.textile.entity.Employee;
import com.example.textile.entity.SalaryStatement;
import com.example.textile.repo.EmployeeRepository;
import com.example.textile.repo.SalaryStatementRepository;
import com.example.textile.service.SalaryStatementService;
import org.springframework.beans.factory.annotation.Autowired;

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
        return statementRepo.findAllOrderByAndLimit(fieldName, pageNumber, pageSize);
    }

    @Override
    public List<SalaryStatement> saveAll(List<SalaryStatement> statements) {
        return statementRepo.saveAll(statements);
    }
}
