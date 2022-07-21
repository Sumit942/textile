package com.example.textile.repo;

import com.example.textile.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT COUNT(*) FROM invoice WHERE invoice_no <> '######'",nativeQuery = true)
    Integer getLastestInvoiceNo();
}
