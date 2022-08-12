package com.example.textile.serviceImpl;

import com.example.textile.entity.*;
import com.example.textile.exception.InvoiceNotFoundException;
import com.example.textile.repo.*;
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

    @Autowired
    TransportModeRepository transportModeRepo;

    @Autowired
    SaleTypeRepository saleTypeRepo;

    @Autowired
    UnitRepository uomRepo;

    @Autowired
    private StateRepository stateRepo;

    @Autowired
    private CompanyRepository companyRepo;

    @Autowired
    private ProductRepository productRepo;

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

    @Override
    public List<TransportMode> getTransportModes() {
        return transportModeRepo.findAll();
    }

    @Override
    public List<SaleType> getSaleTypes() {
        return saleTypeRepo.findAll();
    }

    @Override
    public List<Unit> getUnitOfMeasure() {
        return uomRepo.findAll();
    }

    @Override
    public List<State> getStates() {
        return stateRepo.findAll();
    }

    @Override
    public List<Company> getCompanyByGst(String gst) {
        return companyRepo.findByGst(gst);
    }

    @Override
    public Invoice finById(Long id) {
        return this.invoiceRepo.findById(id).orElse(null);
    }

    @Override
    public Product getProductByName(String name) {
        return this.productRepo.findByName(name);
    }

}
