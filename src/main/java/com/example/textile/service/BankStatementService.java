package com.example.textile.service;

import com.example.textile.entity.Employee;
import com.example.textile.entity.BankStatement;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BankStatementService {

    BankStatement findById(Long id);

    List<BankStatement> findByEmployeeId(Long id);

    void deleteById(Long id);

    BankStatement saveOrUpdate(BankStatement statement);

    List<BankStatement> saveAll(List<BankStatement> statements);

    List<Employee> findAllEmployees();

    List<BankStatement> findAllOrderByAndLimit(String fieldName, int pageNumber, int pageSize);

    Page<BankStatement> findAllByPageNumberAndPageSizeOrderByField(int pageNumber, int pageSize, String field);
}
