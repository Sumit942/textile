package com.example.textile.serviceImpl;

import com.example.textile.entity.Statement;
import com.example.textile.repo.StatementRepository;
import com.example.textile.service.StatementService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class StatementServiceImpl implements StatementService {

    @Autowired
    private StatementRepository statementRepo;

    @Override
    public Statement findById(Long id) {
        return statementRepo.findById(id).orElse(null);
    }

    @Override
    public List<Statement> findByEmployeeId(Long id) {
        return statementRepo.findByEmployeeId(id);
    }

    @Override
    public void deleteById(Long id) {
        statementRepo.deleteById(id);
    }

    @Override
    public Statement saveOrUpdate(Statement statement) {
        return statementRepo.save(statement);
    }

    @Override
    public List<Statement> saveAll(List<Statement> statements) {
        return statementRepo.saveAll(statements);
    }
}
