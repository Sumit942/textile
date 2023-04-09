package com.example.textile.service;

import com.example.textile.entity.Employee;
import com.example.textile.entity.Statement;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StatementService {

    Statement findById(Long id);

    List<Statement> findByEmployeeId(Long id);

    void deleteById(Long id);

    Statement saveOrUpdate(Statement statement);

    List<Statement> saveAll(List<Statement> statements);

    List<Employee> findAllEmployees();

    List<Statement> findAllOrderByAndLimit(String fieldName, int pageNumber, int pageSize);

    Page<Statement> findAllByPageNumberAndPageSizeOrderByField(int pageNumber, int pageSize, String field);
}
