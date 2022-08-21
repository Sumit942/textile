package com.example.textile.serviceImpl;

import com.example.textile.entity.*;
import com.example.textile.exception.InvoiceNotFoundException;
import com.example.textile.repo.*;
import com.example.textile.service.InvoiceService;
import com.example.textile.utility.ShreeramTextileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Autowired
    private BankDetailRepository bankDetailRepo;

    @Override
    public List<Invoice> findAll() {
        return invoiceRepo.findAll();
    }

    @Transactional
    @Override
    public Invoice save(Invoice invoice) {
        String logPrefix = "save() |";
        String logSuffix = "";
        log.info("{} saving.... {}",logPrefix,invoice);
        preCheckCompany(invoice);
        //check if the product name is already added. is yes then use it.
        invoice.getProduct().stream()
                .filter(e -> e.getProduct().getId() == null)
                .forEach(e -> {
                    Product savedPrdt = productRepo.findByName(e.getProduct().getName());
                    if (savedPrdt != null) {
                        e.getProduct().setId(savedPrdt.getId());
                    } else {
                        productRepo.save(e.getProduct());
                        log.info("{} saving product{} ", logPrefix,e.getProduct());
                    }
                });

        Invoice persistedState = cloneInvoice(invoice);
        Invoice saved = invoiceRepo.save(persistedState);
        log.info("waiting 10 sec...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("error: in thread sleep" + e.getLocalizedMessage());
        }
        log.info("continued after 10 sec...");
        return saved;
    }

    private Invoice cloneInvoice(Invoice invoice) {

        if (invoice.isNew())
            return invoice;

        Optional<Invoice> findById = invoiceRepo.findById(invoice.getId());
        if (!findById.isPresent()) {
            return invoice;
        }
        Invoice persisted = findById.get();
        persisted.setInvoiceNo(invoice.getInvoiceNo());
        persisted.setUser(invoice.getUser());
        persisted.setTransportMode(invoice.getTransportMode());
        persisted.setInvoiceDate(invoice.getInvoiceDate());
        persisted.setVehicleNo(invoice.getVehicleNo());
        persisted.setReverseCharge(invoice.getReverseCharge());
        persisted.setDateOfSupply(invoice.getDateOfSupply());
        persisted.setPlaceOfSupply(invoice.getPlaceOfSupply());
        persisted.setBillToParty(invoice.getBillToParty());
        persisted.setShipToParty(invoice.getShipToParty());
        persisted.setSaleType(invoice.getSaleType());
        persisted.setPnfCharge(invoice.getPnfCharge());
        persisted.setTotalAmount(invoice.getTotalAmount());
        persisted.setcGst(invoice.getcGst());
        persisted.setsGst(invoice.getsGst());
        persisted.setTotalTaxAmount(invoice.getTotalTaxAmount());
        persisted.setRoundOff(invoice.getRoundOff());
        persisted.setTotalAmountAfterTax(invoice.getTotalAmountAfterTax());
        persisted.setTotalInvoiceAmountInWords(invoice.getTotalInvoiceAmountInWords());

        persisted.getProduct().clear();
        persisted.getProduct().addAll(invoice.getProduct());

        return persisted;
    }

    private void preCheckCompany(Invoice invoice) {
        String logPrefix = "preCheckCompany() ";
        String logSuffix = "";
        if (invoice.getBillToParty().getId() != null && invoice.getBillToParty().getId() > 0
            && invoice.getShipToParty().getId() != null && invoice.getShipToParty().getId() > 0)
            return;

        //saving transient objects
        if (invoice.getBillToParty().getAddress().getState().getId() == null) {
            State bPartyState = stateRepo.save(invoice.getBillToParty().getAddress().getState());
            invoice.getBillToParty().getAddress().setState(bPartyState);
            //state code are same to bill&ship party then set same state
            if (invoice.getBillToParty().getAddress().getState().getCode()
                    .equals(invoice.getShipToParty().getAddress().getState().getCode())) {
                invoice.getShipToParty().getAddress().setState(bPartyState);
            } else {
                if (invoice.getShipToParty().getAddress().getState().getId() == null) {
                    State sPartyState = stateRepo.save(invoice.getShipToParty().getAddress().getState());
                    invoice.getShipToParty().getAddress().setState(sPartyState);
                }
            }
        }
        if (invoice.getShipToParty().getAddress().getState().getId() == null) {
            State sPartyState = stateRepo.save(invoice.getShipToParty().getAddress().getState());
            invoice.getShipToParty().getAddress().setState(sPartyState);
        }

        // if ship & bill party are same first save Company object for - DataIntegrityViolationException in Company
        if (invoice.getBillToParty().getGst().equalsIgnoreCase(invoice.getShipToParty().getGst())) {
            //if address is different eg. office / company
            logSuffix += "GstMatch=YES;";
            if (!invoice.getBillToParty().getAddress().getAddress().equals(invoice.getShipToParty().getAddress().getAddress())) {
                logSuffix += "AddressMatch=NO;";
                invoice.getBillToParty().setOfcAddress(invoice.getShipToParty().getAddress());
            } else {
                logSuffix += "AddressMatch=YES;";
            }
            Company party = companyRepo.save(invoice.getBillToParty());
            logSuffix += "bsPartyId="+party.getId()+";";
            invoice.setBillToParty(party);
            invoice.setShipToParty(party);
        } else {
            log.info("{} Bill & Ship party different GST", logPrefix);
            Company bParty = companyRepo.save(invoice.getBillToParty());
            Company sParty = companyRepo.save(invoice.getShipToParty());
            invoice.setBillToParty(bParty);
            invoice.setShipToParty(sParty);
        }

        log.info("{} saving-> bParty{}; sParty{}; [{}]", logPrefix, invoice.getBillToParty().getId(), invoice.getShipToParty().getId(), logSuffix);
        log.info("{} Exit {}",logPrefix,logSuffix);
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
    public List<BankDetail> getBankDetailsByGst(String gst) {
        return bankDetailRepo.findByCompanyGst(gst);
    }

}
