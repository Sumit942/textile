package com.example.textile.service;

import com.example.textile.entity.Statement;

import java.util.List;

public interface StatementService {

    Statement findById(Long id);

    List<Statement> findByEmployeeId(Long id);

    void deleteById(Long id);

    Statement saveOrUpdate(Statement statement);

    List<Statement> saveAll(List<Statement> statements);
}
