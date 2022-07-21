package com.example.textile.serviceImpl;

import com.example.textile.entity.Invoice;
import com.example.textile.repo.InvoiceRepository;
import com.example.textile.service.InvoiceService;
import com.example.textile.utility.ShreeramTextileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepo;

    @Override
    public List<Invoice> findAll() {
        return invoiceRepo.findAll();
    }

    @Override
    public Invoice save(Invoice invoice) {
        Invoice saved = invoiceRepo.save(invoice);
        log.info("Invoice [Action=Save, Response=SUCCESS, id={}, invoiceNo={}]",saved.getId(),saved.getInvoiceNo());
        return saved;
    }

    @Override
    public String getLatestInvoiceNo() {

        String latestInvNo;
        int count = invoiceRepo.getLastestInvoiceNo() + 1;

        if (count < 10) {
            latestInvNo = ShreeramTextileConstants.FORMAT_INVOICE_NO + "00" + count;
        } else if (count < 100) {
            latestInvNo = ShreeramTextileConstants.FORMAT_INVOICE_NO + "0" + count;
        } else {
            latestInvNo = ShreeramTextileConstants.FORMAT_INVOICE_NO + count;
        }

        return latestInvNo;
    }
}
