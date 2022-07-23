package com.example.textile.service;

import com.example.textile.entity.*;

import java.util.List;

public interface InvoiceService {

    List<SaleType> getSaleTypes();

    List<TransportMode> getTransportModes();

    List<Invoice> findAll();

    Invoice save(Invoice invoice);

    String getLatestInvoiceNo();

    List<Unit> getUnitOfMeasure();

    List<State> getStates();
}