package com.example.textile.repo;

import com.example.textile.entity.SalaryStatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryStatementRepository extends JpaRepository<SalaryStatement, Long> {
}
