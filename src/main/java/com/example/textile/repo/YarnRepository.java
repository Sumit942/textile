package com.example.textile.repo;

import com.example.textile.entity.Yarn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YarnRepository extends JpaRepository<Yarn, Long> {
    List<Yarn> findByType(String type);

    List<Yarn> findByTypeAndCompanyName(String type, String companyName);
}
