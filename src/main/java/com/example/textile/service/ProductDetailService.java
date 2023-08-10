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

    List<ProductDetail> findByPartyIdAndProductName_Not_Like(Long partyId, String productName);

    List<Long> findAllChNoAndInvoice_IsNull();

    List<ProductDetail> findAllByInvoice(Long invoice);

    List<ProductDetail> findAllByInvoice_Is_Null_And_productName_EndsWith(String prodName);

    List<ProductDetail> findAllByInvoice_Is_Null_And_productName_Not_EndsWith(String prodName);

    List<ProductDetail> challanReport(ProductDetailCommand command);
}
