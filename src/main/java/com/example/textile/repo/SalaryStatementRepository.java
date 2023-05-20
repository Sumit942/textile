package com.example.textile.repo;

import com.example.textile.entity.SalaryStatement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaryStatementRepository extends JpaRepository<SalaryStatement, Long> {
    List<SalaryStatement> findAllOrderByAndLimit(String fieldName, int pageNumber, int pageSize);

}
