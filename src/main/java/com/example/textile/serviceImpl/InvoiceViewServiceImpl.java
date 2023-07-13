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
    public List<InvoiceView> getInvoiceReport(Date fromDate, Date toDate, String invoiceNo, Long companyId) {

        StringBuilder sb = new StringBuilder("select view from InvoiceView view where view.invoiceId > 0 ");
        if (fromDate != null) {
            sb.append("and view.invoiceDate >= :fromDate ");
        }
        if (toDate != null) {
            sb.append("and view.invoiceDate <= :toDate ");
        }
        if (invoiceNo != null && !invoiceNo.isEmpty()) {
            sb.append("and view.invoiceNo like concat('%',lower(:invoiceNo)) ");
        }
        if (companyId != null) {
            sb.append("and view.billToPartyId = :companyId ");
        }
        sb.append("order by view.invoiceId desc");
        Query query = entityManager.createQuery(sb.toString());

        if (fromDate != null) {
            query.setParameter("fromDate", fromDate, TemporalType.DATE);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate, TemporalType.DATE);
        }
        if (invoiceNo != null && !invoiceNo.isEmpty()) {
            query.setParameter("invoiceNo", invoiceNo);
        }
        if (companyId != null) {
            query.setParameter("companyId", companyId);
        }

        return (List<InvoiceView>) query.getResultList();
    }

    @Override
    public List<InvoiceView> findByInvoiceId(List<Long> invoiceId) {
        return viewRepository.findByInvoiceId(invoiceId);
    }
}
