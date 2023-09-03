package com.example.textile.serviceImpl;

import com.example.textile.entity.Product;
import com.example.textile.entity.Yarn;
import com.example.textile.entity.YarnDesign;
import com.example.textile.repo.ProductRepository;
import com.example.textile.repo.YarnDesignRepository;
import com.example.textile.repo.YarnRepository;
import com.example.textile.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepo;

    @Autowired
    YarnRepository yarnRepo;

    @Autowired
    YarnDesignRepository yarnDesignRepo;

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
    public List<Product> findByNameAndLimit(String name, int limit, Boolean active) {
        StringBuilder sb = new StringBuilder("select p from Product p where lower(p.name) like lower(:name)");
        if (Objects.nonNull(active)) {
            sb.append(" and p.active = :active");
        }
        Query query = entityManager
                .createQuery(sb.toString());
        if (Objects.nonNull(active)) {
            query.setParameter("active",active);
        }
        if (limit > 0) {
            query.setMaxResults(limit);
        }
        query.setParameter("name","%"+name+"%");
        return (List<Product>) query.getResultList();
    }

    @Override
    public Product findById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    @Override
    public Product save(Product product) {
        return productRepo.save(product);
    }

    @Override
    public List<Yarn> findAllYarn() {
        return yarnRepo.findAll();
    }

    @Override
    public List<YarnDesign> findAllYarnDesign() {
        return yarnDesignRepo.findAll();
    }
}
