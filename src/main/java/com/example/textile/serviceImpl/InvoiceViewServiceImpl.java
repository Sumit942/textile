package com.example.textile.serviceImpl;

import com.example.textile.entity.InvoiceView;
import com.example.textile.repo.InvoiceViewRepository;
import com.example.textile.service.InvoiceViewService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
