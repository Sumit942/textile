package com.example.textile.service;

import com.example.textile.entity.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InvoiceService {

    List<SaleType> getSaleTypes();

    List<TransportMode> getTransportModes();

    List<Invoice> findAll();

    Invoice save(Invoice invoice);

    Invoice saveOrUpdate(Invoice invoice);

    String getLatestInvoiceNo();

    List<Unit> getUnitOfMeasure();

    List<State> getStates();

    List<Company> getCompanyByGst(String gst);

    Invoice finById(Long id);

    List<BankDetail> getBankDetailsByGst(String gst);

    List<Invoice> findByInvoiceNo(String invoiceNo);
}
