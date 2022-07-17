package com.example.textile.repo;

import com.example.textile.entity.BankDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankDetailRepository extends JpaRepository<BankDetail, Long> {
}
