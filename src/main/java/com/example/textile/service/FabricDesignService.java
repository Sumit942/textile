package com.example.textile.service;

import com.example.textile.entity.FabricDesign;

import java.util.List;

public interface FabricDesignService {
    FabricDesign findById(Long id);
    FabricDesign saveOrUpdate(FabricDesign fabricDesign);
    void deleteById(Long id);

    List<FabricDesign> findAll();
}
