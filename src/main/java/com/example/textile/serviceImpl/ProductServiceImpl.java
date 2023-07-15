package com.example.textile.serviceImpl;

import com.example.textile.entity.Product;
import com.example.textile.repo.ProductRepository;
import com.example.textile.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepo;

    @Autowired
    EntityManager entityManager;

    @Override
    public Product findByName(String name) {
        return this.productRepo.findByNameIgnoreCase(name);
    }

    @Override
    public List<Product> findByNameLike(String name) {
        return this.productRepo.findByNameLike(name);
    }

    @Override
    public List<Product> findByNameAndLimit(String name, int limit) {
        Query query = entityManager
                .createQuery("select p from Product p where lower(p.name) like lower(:name) and p.active = true");
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        query.setParameter("name","%"+name+"%");
        return (List<Product>) query.getResultList();
    }
}
