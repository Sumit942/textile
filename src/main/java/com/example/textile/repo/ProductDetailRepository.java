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

    @Query("SELECT pd.chNo FROM ProductDetail pd WHERE pd.invoice IS NULL ORDER BY pd.chNo ASC")
    List<Long> findAllChNoAndInvoice_IsNull();

    List<ProductDetail> findByPartyIdAndInvoiceId(Long partyId,Long invoiceId);

    @Query("FROM ProductDetail pd WHERE pd.party.id=:partyId AND upper(pd.product.name) NOT LIKE upper(:productName) AND pd.invoice IS NULL")
    List<ProductDetail> findByPartyIdAndProductName_Not_Like(Long partyId,String productName);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProductDetail pd WHERE pd.chNo IN (:id) AND pd.invoice IS NULL")
    void deleteAllByChNoAndInvoice_IsNull(List<Long> id);

    List<ProductDetail> findAllByInvoice(Long invoice);

    @Query("FROM ProductDetail pd WHERE upper(pd.product.name) LIKE upper(:productName) AND pd.invoice IS NULL")
    List<ProductDetail> findAllByInvoice_Is_Null_And_productName_EndsWith(String productName);

    @Query("FROM ProductDetail pd WHERE upper(pd.product.name) NOT LIKE upper(:productName) AND pd.invoice IS NULL")
    List<ProductDetail> findAllByInvoice_Is_Null_And_productName_Not_EndsWith(String productName);
}
