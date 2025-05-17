package com.example.textile.repo;

import com.example.textile.dto.YarnFabricDesignDto;
import com.example.textile.entity.YarnFabricDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface YarnFabricDesignRepo extends JpaRepository<YarnFabricDesign, Long> {
    boolean existsByQualityName(String qualityName);

    @Query("SELECT new com.example.textile.dto.YarnFabricDesignDto(yfd.id, yfd.qualityName) FROM YarnFabricDesign yfd")
    List<YarnFabricDesignDto> findAllYarnFabric();

    @Query("SELECT yfd.id FROM YarnFabricDesign yfd WHERE UPPER(yfd.qualityName)=UPPER(:qualityName)")
    Long getYarnFabricDesignIdByQualityName(String qualityName);
}
