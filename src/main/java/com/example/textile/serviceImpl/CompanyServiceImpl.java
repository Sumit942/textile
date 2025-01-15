package com.example.textile.serviceImpl;

import com.example.textile.dto.CompanyDto;
import com.example.textile.entity.Company;
import com.example.textile.exception.CompanyNotFoundException;
import com.example.textile.repo.CompanyRepository;
import com.example.textile.service.CompanyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepo;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Override
    public List<Company> findByNameLike(String name) {
        return this.companyRepo.findByNameLike(name);
    }

    @Override
    public Company save(Company company) {
        log.info("save() | "+company);
        return this.companyRepo.save(company);
    }

    @Override
    public List<Company> findAll() {
        return this.companyRepo.findAll();
    }

    @Override
    public Company findById(Long id) {
        return this.companyRepo.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
    }

    @Override
    public CompanyDto save(CompanyDto companyDto) {
        log.info("save() dto: " + companyDto);
        Company company = modelMapper.map(companyDto, Company.class);
        company = this.companyRepo.save(company);

        return modelMapper.map(company, CompanyDto.class);
    }

    @Override
    public List<CompanyDto> getIdNameAndGstByName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CompanyDto> query = cb.createQuery(CompanyDto.class);
        Root<Company> companyRoot = query.from(Company.class);

        query.select(cb.construct(CompanyDto.class,
                companyRoot.get("id"),
                companyRoot.get("name"),
                companyRoot.get("gst")))
                .where(cb.like(cb.lower(companyRoot.get("name")), "%"+name.toLowerCase()+"%"));
        return entityManager.createQuery(query).getResultList();
    }
}
