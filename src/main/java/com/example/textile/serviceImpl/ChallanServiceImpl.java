package com.example.textile.serviceImpl;

import com.example.textile.entity.Challan;
import com.example.textile.exception.ChallanNotFoundException;
import com.example.textile.repo.ChallanRepository;
import com.example.textile.service.ChallanService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ChallanServiceImpl implements ChallanService {

    @Autowired
    ChallanRepository repo;

    public Challan save(Challan challan) {
        return repo.save(challan);
    }

    public List<Challan> saveAll(List<Challan> challans) {
        return repo.saveAll(challans);
    }

    public List<Challan> findAll() {
        return repo.findAll();
    }

    public Challan findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ChallanNotFoundException(id));
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
