package com.example.textile.service;

import com.example.textile.command.ProductDetailCommand;
import com.example.textile.entity.ProductDetail;
import com.example.textile.entity.Unit;

import java.util.List;

public interface ProductDetailService {

    List<Unit> findAllUnit();

    List<ProductDetail> saveAll(List<ProductDetail> productDetails);

    List<ProductDetail> findByChNo(Long s);

    List<Long> findAllChNo();

    List<ProductDetail> findByPartyIdAndInvoiceId(Long partyId, Long invoiceId);

    List<ProductDetail> findAllByInvoice(Long invoice);

    List<ProductDetail> challanReport(ProductDetailCommand command);

    List<ProductDetail> findAllUnbilledByPartyId(Long id, List<Long> challans);

    List<ProductDetail> findAllExcluded();

    List<ProductDetail> findByChNos(List<Long> challanNos);
}
