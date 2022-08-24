package com.example.textile.repo;

import com.example.textile.entity.Invoice;
import com.example.textile.entity.InvoiceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT COUNT(*) FROM invoice WHERE invoice_no <> '######' or invoice_no is not null",nativeQuery = true)
    Integer getLatestInvoiceNo();

    @Query("SELECT new com.example.textile.entity.InvoiceView(i.id,i.invoiceDate,i.invoiceNo,i.billToParty.gst," +
            "i.billToParty.name,i.totalAmount,i.cGst,i.sGst,i.roundOff,i.pnfCharge,i.totalAmountAfterTax) FROM Invoice i")
    List<InvoiceView> viewList();

    List<Invoice> findByInvoiceNo(String invoiceNo);

    Integer countByInvoiceNo(String latestInvNo);
}
