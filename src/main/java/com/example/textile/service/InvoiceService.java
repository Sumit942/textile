package com.example.textile.service;

import com.example.textile.entity.Invoice;

import java.util.List;

public interface InvoiceService {

    List<Invoice> findAll();

    Invoice save(Invoice invoice);

    String getLatestInvoiceNo();
}
