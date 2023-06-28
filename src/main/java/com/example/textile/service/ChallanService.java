package com.example.textile.service;

import com.example.textile.entity.Challan;
import com.example.textile.repo.FabricDesignRepo;
import com.example.textile.repo.MachineRepo;
import com.example.textile.repo.SaleTypeRepository;
import com.example.textile.repo.YarnRepository;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChallanService {

    SaleTypeRepository getSaleTypeRepo();

    MachineRepo getMachineRepo();

    YarnRepository getYarnRepo();

    FabricDesignRepo getFabricDesignRepo();

    Challan save(Challan challan);

    List<Challan> saveAll(List<Challan> challans);

    List<Challan> findAll();

    Challan findById(Long id);

    void deleteById(Long id);

    Page<Challan> findAllByPageNumberAndPageSizeOrderByField(Integer pageNumber, Integer pageSize, String fieldName);

    Challan findByChallanNo(Long challanNo);
}
