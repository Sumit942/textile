package com.example.textile.serviceImpl;

import com.example.textile.entity.Challan;
import com.example.textile.exception.ChallanNotFoundException;
import com.example.textile.repo.ChallanRepository;
import com.example.textile.service.ChallanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChallanServiceImpl implements ChallanService {

    @Autowired
    ChallanRepository repo;

    @Override
    public Challan save(Challan challan) {
        return repo.save(challan);
    }

    @Override
    public List<Challan> saveAll(List<Challan> challans) {
        return repo.saveAll(challans);
    }

    @Override
    public List<Challan> findAll() {
        return repo.findAll();
    }

    @Override
    public Challan findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new ChallanNotFoundException(id));
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Page<Challan> findAllByPageNumberAndPageSizeOrderByField(Integer pageNumber, Integer pageSize, String fieldName) {
        return repo.findAll(PageRequest.of(pageNumber, pageSize).withSort(Sort.by(fieldName).descending()));
    }
}
