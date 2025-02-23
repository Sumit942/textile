package com.example.textile.serviceImpl;

import com.example.textile.dto.YarnDto;
import com.example.textile.entity.Company;
import com.example.textile.entity.Yarn;
import com.example.textile.repo.CompanyRepository;
import com.example.textile.repo.YarnRepository;
import com.example.textile.service.YarnService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class YarnServiceImpl implements YarnService {

    private YarnRepository yarnRepo;
    private CompanyRepository companyRepository;
    private ModelMapper modelMapper;
    private EntityManager entityManager;

    @Override
    public List<YarnDto> findAll() {
        return yarnRepo.findAll()
                .stream().map(yarn -> modelMapper.map(yarn, YarnDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public YarnDto findById(Long id) {
        Yarn persistedYarn = yarnRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Yarn not found [id= " + id + "]"));
        return modelMapper.map(persistedYarn, YarnDto.class);
    }

    @Override
    public YarnDto save(YarnDto yarnDto) {
        Yarn yarn = modelMapper.map(yarnDto, Yarn.class);
        if (Objects.nonNull(yarnDto.getCompany()) && Objects.isNull(yarnDto.getCompany().getId())) {
            Company company = companyRepository.save(modelMapper.map(yarnDto.getCompany(), Company.class));
            yarn.setCompany(company);
        }

        Yarn saved = yarnRepo.save(yarn);
        return modelMapper.map(saved, YarnDto.class);
    }

    @Override
    public YarnDto updateYarn(Long id, YarnDto yarnDto) {
        Yarn persistedYarn = yarnRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Yarn not found [id= " + id + "]"));

        yarnDto.setId(id);
        modelMapper.map(yarnDto, persistedYarn);
        persistedYarn = yarnRepo.save(persistedYarn);
        return modelMapper.map(persistedYarn, YarnDto.class);
    }

    @Override
    public void deleteYarn(Long id) {
        yarnRepo.deleteById(id);
    }

    @Override
    public boolean existByYarnTypeAndCompanyNameIgnoreCase(YarnDto yarnDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Yarn> yarn = cq.from(Yarn.class);
        List<Predicate> predicates = new ArrayList<>();

        Predicate yarnTypePredicate = cb.equal(cb.lower(yarn.get("type")), yarnDto.getType().toLowerCase());
        predicates.add(yarnTypePredicate);

        Predicate namePredicate;
        if (Objects.nonNull(yarnDto.getCompany())) {
            if (Objects.nonNull(yarnDto.getCompany().getId()) && yarnDto.getCompany().getId().compareTo(0L) > 0) {
                namePredicate = cb.equal(yarn.get("company").get("id"), yarnDto.getCompany().getId());
            } else if (Objects.nonNull(yarnDto.getCompany().getGst()) && !yarnDto.getCompany().getGst().isBlank()) {
                namePredicate = cb.equal(yarn.get("company").get("gst"), yarnDto.getCompany().getGst());
            } else {
                namePredicate = cb.isNull(yarn.get("company"));
            }
        } else {
            namePredicate = cb.isNull(yarn.get("company"));
        }
        predicates.add(namePredicate);

        if (Objects.nonNull(yarnDto.getId()) && yarnDto.getId().compareTo(0L) > 0) {
            Predicate idPredicate = cb.notEqual(yarn.get("id"), yarnDto.getId());
            predicates.add(idPredicate);
        }

        cq.select(cb.count(yarn)).where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(cq).getSingleResult() > 0;

    }

    @Override
    public List<YarnDto> findByTypeLike(String type) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<YarnDto> cq = cb.createQuery(YarnDto.class);
        Root<Yarn> root = cq.from(Yarn.class);

        cq.select(cb.construct(YarnDto.class,
                root.get("id"),
                root.get("type")))
                .where(cb.like(cb.lower(root.get("type")), "%"+type.toLowerCase()+"%"));

        return entityManager.createQuery(cq).getResultList();
    }
}
