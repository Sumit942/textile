package com.example.textile.repo;

import com.example.textile.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    List<ProductDetail> findByChNo(Long chNo);

    @Query("SELECT pd.chNo FROM ProductDetail pd WHERE pd.chNo IS NOT NULL")
    List<Long> findAllChNo();

    List<ProductDetail> findByPartyIdAndInvoiceId(Long partyId,Long invoiceId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductDetail pd WHERE pd.chNo IN (:id) AND pd.invoice IS NULL")
    void deleteAllByChNoAndInvoice_IsNull(List<Long> id);
}
