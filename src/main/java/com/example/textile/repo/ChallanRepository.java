package com.example.textile.repo;

import com.example.textile.entity.Challan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallanRepository extends JpaRepository<Challan, Long> {
    Challan findByChallanNo(Long challanNo);
}
