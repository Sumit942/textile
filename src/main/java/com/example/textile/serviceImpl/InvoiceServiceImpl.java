package com.example.textile.serviceImpl;

import com.example.textile.entity.*;
import com.example.textile.repo.*;
import com.example.textile.service.InvoiceService;
import com.example.textile.utility.ShreeramTextileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    @Value("${invoice.startCount}")
    private Integer invoiceStartCount;

    @Override
    public List<Invoice> findAll() {
        return invoiceRepo.findAll();
    }

    @Transactional
    @Override
    public Invoice saveOrUpdate(Invoice invoice) {
        String logPrefix = "save() |";
        String logSuffix = "";
        boolean isNew = invoice.isNew();
        log.info("{} saving.... {}",logPrefix,invoice.getInvoiceNo());
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
        Invoice saved = save(persistedState);
        /*if (isNew) {
            //this code is added for giving a custom invoiceNo which is missed in between
            if (invoice.getInvoiceNo() != null && !invoice.getInvoiceNo().isEmpty()) {
                List<Invoice> byInvoiceNo = invoiceRepo.findByInvoiceNo(saved.getInvoiceNo());
                if (byInvoiceNo != null && !byInvoiceNo.isEmpty()) {
                    for (Invoice value : byInvoiceNo) {
                        if (!value.getId().equals(saved.getId())) {
                            invoice.setInvoiceNo(getLatestInvoiceNo());
                            break;
                        }
                    }
                }
            } else {
                invoice.setInvoiceNo(getLatestInvoiceNo());
            }

        }*/
        if (isNew && (invoice.getInvoiceNo() == null || invoice.getInvoiceNo().isEmpty())) {
            invoice.setInvoiceNo(getLatestInvoiceNo());
        }
        return saved;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Invoice save(Invoice invoice) {
        return invoiceRepo.save(invoice);
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
        persisted.setiGst(invoice.getiGst());
        persisted.setGstPerc(invoice.getGstPerc());
        persisted.setTotalTaxAmount(invoice.getTotalTaxAmount());
        persisted.setRoundOff(invoice.getRoundOff());
        persisted.setTotalAmountAfterTax(invoice.getTotalAmountAfterTax());
        persisted.setTotalInvoiceAmountInWords(invoice.getTotalInvoiceAmountInWords());

        persisted.getProduct().clear();
        persisted.getProduct().addAll(invoice.getProduct());

        persisted.setSelectedBank(invoice.getSelectedBank());

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
        int count = invoiceRepo.getLatestInvoiceNo() + invoiceStartCount;
        String latestInvNo = getFormattedInvoiceNo(count);

        while (invoiceRepo.countByInvoiceNo(latestInvNo) > 0) {
            latestInvNo = getFormattedInvoiceNo(++count);
        }
        log.info(">>latestInvNo: "+latestInvNo);

        return latestInvNo;
    }

    public String getFormattedInvoiceNo(int count) {
        String latestInvNo;
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

    @Override
    public List<Invoice> findByInvoiceNo(String invoiceNo) {
        return invoiceRepo.findByInvoiceNo(invoiceNo);
    }

    @Override
    public void deleteById(Long id) {
        invoiceRepo.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteByInvoiceNo(String invoiceNo) {
        invoiceRepo.deleteByInvoiceNo(invoiceNo);
    }
}
