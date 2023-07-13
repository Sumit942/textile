package com.example.textile.repo;

import com.example.textile.entity.InvoiceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceViewRepository extends JpaRepository<InvoiceView,Long> {

    @Override
    @Query("FROM InvoiceView view ORDER BY view.invoiceNo DESC")
    List<InvoiceView> findAll();

    List<InvoiceView> findByInvoiceId(List<Long> invoiceId);
}
