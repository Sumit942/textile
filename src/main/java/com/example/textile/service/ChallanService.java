package com.example.textile.service;

import com.example.textile.entity.Challan;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChallanService {
    Challan save(Challan challan);

    List<Challan> saveAll(List<Challan> challans);

    List<Challan> findAll();

    Challan findById(Long id);

    void deleteById(Long id);

    Page<Challan> findAllByPageNumberAndPageSizeOrderByField(Integer pageNumber, Integer pageSize, String fieldName);
}
