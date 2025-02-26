package com.example.textile.repo;

import com.example.textile.entity.YarnFabricDesign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YarnFabricDesignRepo extends JpaRepository<YarnFabricDesign, Long> {
    boolean existsByQualityName(String qualityName);
}
