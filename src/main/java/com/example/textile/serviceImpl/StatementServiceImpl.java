package com.example.textile.serviceImpl;

import com.example.textile.entity.Employee;
import com.example.textile.entity.Statement;
import com.example.textile.repo.EmployeeRepository;
import com.example.textile.repo.StatementRepository;
import com.example.textile.service.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementServiceImpl implements StatementService {

    @Autowired
    private StatementRepository statementRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

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

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public List<Statement> findAllOrderByAndLimit(String fieldName, int pageNumber, int pageSize) {
        return findAllByPageNumberAndPageSizeOrderByField(pageNumber, pageSize, fieldName).getContent();
    }

    @Override
    public Page<Statement> findAllByPageNumberAndPageSizeOrderByField(int pageNumber, int pageSize, String field) {
        return statementRepo.findAll(PageRequest.of(pageNumber, pageSize).withSort(Sort.by(field).descending()));
    }
}
