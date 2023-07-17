package com.example.textile.repo;

import com.example.textile.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    List<ProductDetail> findByChNo(String chNo);

    @Query("SELECT pd.chNo FROM ProductDetail pd WHERE pd.chNo IS NOT NULL")
    List<String> findAllChNo();

    List<ProductDetail> findByPartyIdAndInvoiceId(Long partyId,Long invoiceId);
}
