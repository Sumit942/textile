package com.example.textile.serviceImpl;

import com.example.textile.entity.InvoiceView;
import com.example.textile.repo.InvoiceViewRepository;
import com.example.textile.service.InvoiceViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Service
public class InvoiceViewServiceImpl implements InvoiceViewService {

    @Autowired
    InvoiceViewRepository viewRepository;

    @Override
    public List<InvoiceView> findAll() {
        return viewRepository.findAll();
    }

    @Override
    public List<InvoiceView> findAllOrderByAndLimit(String fieldName, int pageNumber, int pageSize) {
        return findAllByPageNumberAndPageSizeOrderByField(pageNumber, pageSize, fieldName).getContent();
    }

    @Override
    public Page<InvoiceView> findAllByPageNumberAndPageSizeOrderByField(int pageNumber, int pageSize, String fieldName) {
        return viewRepository
                .findAll(PageRequest.of(pageNumber, pageSize).withSort(Sort.by(fieldName).descending()));
    }


    @Autowired
    EntityManager entityManager;

    @Override
    public List<InvoiceView> getInvoiceReport(Date fromDate, Date toDate, List<String> invoiceNos, Long companyId, Boolean paymentStatus) {

        StringBuilder sb = new StringBuilder("select view from InvoiceView view where view.invoiceId > 0 ");
        if (fromDate != null) {
            sb.append("and view.invoiceDate >= :fromDate ");
        }
        if (toDate != null) {
            sb.append("and view.invoiceDate <= :toDate ");
        }
        if (invoiceNos != null && !invoiceNos.isEmpty()) {
            for (int i = 0; i < invoiceNos.size(); i++) {
                if (i == 0) {
                    sb.append(" AND");
                    if (invoiceNos.size() > 1)
                        sb.append(" (");
                } else {
                    sb.append(" OR");
                }
                sb.append(" lower(view.invoiceNo) like lower(:invoiceNo").append(i).append(") ");
            }
            if (invoiceNos.size() > 1)
                sb.append(") ");
        }
        if (companyId != null) {
            sb.append("and view.billToPartyId = :companyId ");
        }
        if (paymentStatus != null) {
            sb.append("and view.paid = :paid ");
        }
        sb.append("order by view.invoiceId ASC");
        TypedQuery<InvoiceView> query = entityManager.createQuery(sb.toString(), InvoiceView.class);

        if (fromDate != null) {
            query.setParameter("fromDate", fromDate, TemporalType.DATE);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate, TemporalType.DATE);
        }
        if (invoiceNos != null && !invoiceNos.isEmpty()) {
            for (int i = 0; i < invoiceNos.size(); i++) {
                query.setParameter("invoiceNo"+i,"%"+invoiceNos.get(i));
            }
        }
        if (companyId != null) {
            query.setParameter("companyId", companyId);
        }
        if (paymentStatus != null) {
            query.setParameter("paid", paymentStatus);
        }

        return query.getResultList();
    }

    @Override
    public List<InvoiceView> findByInvoiceId(List<Long> invoiceId) {
        return viewRepository.findByInvoiceId(invoiceId);
    }
}
