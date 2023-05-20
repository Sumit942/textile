package com.example.textile.serviceImpl;

import com.example.textile.entity.BankStatement;
import com.example.textile.entity.Employee;
import com.example.textile.repo.EmployeeRepository;
import com.example.textile.repo.StatementRepository;
import com.example.textile.service.BankStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankStatementServiceImpl implements BankStatementService {

    @Autowired
    private StatementRepository statementRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Override
    public BankStatement findById(Long id) {
        return statementRepo.findById(id).orElse(null);
    }

    //TODO: fetch employee with statement
    @Override
    public List<BankStatement> findByEmployeeId(Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        statementRepo.deleteById(id);
    }

    @Override
    public BankStatement saveOrUpdate(BankStatement statement) {
        return statementRepo.save(statement);
    }

    @Override
    public List<BankStatement> saveAll(List<BankStatement> statements) {
        return statementRepo.saveAll(statements);
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public List<BankStatement> findAllOrderByAndLimit(String fieldName, int pageNumber, int pageSize) {
        return findAllByPageNumberAndPageSizeOrderByField(pageNumber, pageSize, fieldName).getContent();
    }

    @Override
    public Page<BankStatement> findAllByPageNumberAndPageSizeOrderByField(int pageNumber, int pageSize, String field) {
        return statementRepo.findAll(PageRequest.of(pageNumber, pageSize).withSort(Sort.by(field).descending()));
    }
}
