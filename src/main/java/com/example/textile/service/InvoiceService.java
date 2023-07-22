package com.example.textile.service;

import com.example.textile.entity.*;

import java.util.List;

public interface InvoiceService {

    List<SaleType> getSaleTypes();

    List<TransportMode> getTransportModes();

    Integer getOldInvoiceLastId();

    List<ProductDetail> findByChNo(Long chNo);

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

    void deleteById(Long id);

    void deleteByInvoiceNo(String invoiceNo);

    void deleteProductDetailsByChNoAndInvoice_isNull(List<Long> challanNo);
}
