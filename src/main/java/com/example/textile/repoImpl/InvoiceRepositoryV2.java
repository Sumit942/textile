package com.example.textile.repoImpl;

import com.example.textile.entity.Invoice;
import com.example.textile.entity.InvoiceView;
import com.example.textile.repo.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

//@Repository
public abstract class InvoiceRepositoryV2 implements InvoiceRepository {

    @Autowired
    EntityManager entityManager;

    @Override
    public List<InvoiceView> viewList() {

        String sqlQuery = "SELECT i.id,i.invoiceDate,i.invoiceNo,i.billToParty.gst,i.billToParty.name," +
                "i.totalAmount,i.cGst,i.sGst,i.roundOff,i.pnfCharge,i.totalAmountAfterTax FROM Invoice i";
        Query query = entityManager.createQuery(sqlQuery);
        List<InvoiceView> invoiceViews = query.getResultList();
        return invoiceViews;
    }
}
