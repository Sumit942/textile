package com.example.textile.repo;

import com.example.textile.entity.Statement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatementRepository extends JpaRepository<Statement, Long> {
    List<Statement> findByEmployeeId(Long id);
}
