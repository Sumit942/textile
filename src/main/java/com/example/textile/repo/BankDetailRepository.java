package com.example.textile.repo;

import com.example.textile.entity.BankDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankDetailRepository extends JpaRepository<BankDetail, Long> {

    List<BankDetail> findByCompanyGst(String gst);
}
