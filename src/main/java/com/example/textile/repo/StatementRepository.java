package com.example.textile.repo;

import com.example.textile.entity.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatementRepository extends JpaRepository<BankStatement, Long> {
}
