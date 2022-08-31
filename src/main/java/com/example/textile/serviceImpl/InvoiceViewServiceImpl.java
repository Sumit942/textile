package com.example.textile.serviceImpl;

import com.example.textile.entity.InvoiceView;
import com.example.textile.repo.InvoiceViewRepository;
import com.example.textile.service.InvoiceViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceViewServiceImpl implements InvoiceViewService {

    @Autowired
    InvoiceViewRepository viewRepository;

    @Override
    public List<InvoiceView> findAll() {
        return viewRepository.findAll();
    }

    @Override
    public List<InvoiceView> findAllOrderByAndLimit(String fieldName, int pageNumber, int pageSize) {
        return viewRepository
                .findAll(PageRequest.of(pageNumber, pageSize).withSort(Sort.by(fieldName).descending()))
                .getContent();
    }
}
