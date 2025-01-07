package com.example.textile.serviceImpl;

import com.example.textile.dto.YarnDto;
import com.example.textile.entity.Yarn;
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
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class YarnServiceImpl implements YarnService {

    private YarnRepository yarnRepo;
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
    public List<YarnDto> findByType(String type) {
        List<Yarn> byType = yarnRepo.findByType(type);
        if (Objects.nonNull(byType) && !byType.isEmpty()) {
            return byType.stream()
                    .map(yarn -> modelMapper.map(yarn, YarnDto.class))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<YarnDto> findByTypeAndCompanyName(String type, String companyName) {
        List<Yarn> byType = yarnRepo.findByTypeAndCompanyName(type, companyName);
        if (Objects.nonNull(byType) && !byType.isEmpty()) {
            return byType.stream()
                    .map(yarn -> modelMapper.map(yarn, YarnDto.class))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean existByYarnTypeAndCompanyNameIgnoreCase(@NotNull String type, String companyName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Yarn> yarn = cq.from(Yarn.class);

        Predicate yarnTypePredicate = cb.equal(cb.lower(yarn.get("type")), type.toLowerCase());

        Predicate companyNamePredicate;
        if (Objects.nonNull(companyName) && !companyName.isBlank()) {
            companyNamePredicate = cb.equal(cb.lower(yarn.get("companyName")), companyName.trim().toLowerCase());
        } else {
            companyNamePredicate = cb.or(cb.isNull(yarn.get("companyName")), cb.equal(yarn.get("companyName"),""));
        }

        cq.select(cb.count(yarn)).where(cb.and(yarnTypePredicate, companyNamePredicate));

        return entityManager.createQuery(cq).getSingleResult() > 0;

    }
}
