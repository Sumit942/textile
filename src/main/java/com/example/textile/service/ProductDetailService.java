package com.example.textile.service;

import com.example.textile.entity.ProductDetail;
import com.example.textile.entity.Unit;

import java.util.List;

public interface ProductDetailService {

    List<Unit> findAllUnit();

    List<ProductDetail> saveAll(List<ProductDetail> productDetails);

    List<ProductDetail> findByChNo(Long s);

    List<Long> findAllChNo();

    List<ProductDetail> findByPartyIdAndInvoiceId(Long partyId, Long invoiceId);

    List<Long> findAllChNoAndInvoice_IsNull();

    List<ProductDetail> findAllByInvoice(Long invoice);
}
