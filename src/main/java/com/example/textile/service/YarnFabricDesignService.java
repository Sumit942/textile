package com.example.textile.service;

import com.example.textile.entity.YarnFabricDesign;

import java.util.List;

public interface YarnFabricDesignService {
    List<YarnFabricDesign> findByYarnTypesAndFabricDesign(String yarnsFabricDesign);

    YarnFabricDesign save(YarnFabricDesign yarnFabricDesign);

    void deleteById(Long id);

    boolean existsByQualityName(String qualityName);
}
