package com.example.textile.service;

import com.example.textile.entity.InvoiceView;

import java.util.List;

public interface InvoiceViewService {

    List<InvoiceView> findAll();

    List<InvoiceView> findAllOrderByAndLimit(String fieldName, int pageNumber, int pageSize);
}
