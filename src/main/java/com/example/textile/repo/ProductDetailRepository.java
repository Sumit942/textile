package com.example.textile.repo;

import com.example.textile.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
    List<ProductDetail> findByChNo(Long chNo);

    @Query("SELECT pd.chNo FROM ProductDetail pd WHERE pd.chNo IS NOT NULL ORDER BY pd.chNo ASC")
    List<Long> findAllChNo();

    @Query("SELECT pd.chNo FROM ProductDetail pd WHERE pd.invoice IS NULL ORDER BY pd.chNo ASC")
    List<Long> findAllChNoAndInvoice_IsNull();

    List<ProductDetail> findByPartyIdAndInvoiceId(Long partyId,Long invoiceId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductDetail pd WHERE pd.chNo IN (:id) AND pd.invoice IS NULL")
    void deleteAllByChNoAndInvoice_IsNull(List<Long> id);

    List<ProductDetail> findAllByInvoice(Long invoice);

    @Query("FROM ProductDetail pd WHERE pd.chNo IN (:chNos)")
    List<ProductDetail> findByChNos(List<Long> chNos);
}
