package com.example.textile.service;

import com.example.textile.entity.InvoiceView;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InvoiceViewService {

    List<InvoiceView> findAll();

    List<InvoiceView> findAllOrderByAndLimit(String fieldName, int pageNumber, int pageSize);

    Page<InvoiceView> findAllByPageNumberAndPageSizeOrderByField(int pageNumber, int pageSize, String fieldName);
}
