package com.example.textile.service;

import com.example.textile.entity.Employee;
import com.example.textile.entity.SalaryStatement;

import java.util.List;

public interface SalaryStatementService {
    List<Employee> findAllEmployees();

    List<SalaryStatement> findAllOrderByAndLimit(String txnDt, int pageNumber, int pageSize);

    List<SalaryStatement> saveAll(List<SalaryStatement> statements);
}
