package com.example.textile.serviceimpl;

import com.example.textile.entity.FabricDesignYarnMapping;
import com.example.textile.entity.YarnFabricDesign;
import com.example.textile.repo.YarnFabricDesignRepo;
import com.example.textile.service.YarnFabricDesignService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class YarnFabricDesignServiceImpl implements YarnFabricDesignService {
    private YarnFabricDesignRepo yarnFabricDesignRepo;
    private EntityManager entityManager;

    @Override
    public List<YarnFabricDesign> findByYarnTypesAndFabricDesign(String yarnFabricDesign, boolean isDeepSearch) {
        if (isDeepSearch) {
            return findByYarnTypesAndFabricDesign(yarnFabricDesign);
        }
        if (StringUtils.isEmpty(yarnFabricDesign)) {
            return Collections.emptyList();
        }

        String yarns = null, fabric = null;
        if (yarnFabricDesign.startsWith("--")) {
            fabric = yarnFabricDesign.replace("--","");
        } else {
            String[] yarnsAndFabricArr = yarnFabricDesign.split("--");
            yarns = yarnsAndFabricArr[0];
            if (yarnsAndFabricArr.length > 1) {
                fabric = yarnsAndFabricArr[1];
            }
        }


        StringBuilder sb = new StringBuilder("SELECT yfd FROM YarnFabricDesign yfd WHERE ");
        if (!StringUtils.isEmpty(yarns)) {
            sb.append("LOWER(yfd.qualityName) like LOWER(:yarns) ");
        }
        if (!StringUtils.isEmpty(fabric)) {
            if (!StringUtils.isEmpty(yarns)) {
                sb.append("AND ");
            }
            sb.append("LOWER(yfd.fabricDesign.name) like LOWER(:fabricDesignName)");
        }

        TypedQuery<YarnFabricDesign> query = entityManager.createQuery(sb.toString(), YarnFabricDesign.class);
        if (!StringUtils.isEmpty(yarns)) {
            query.setParameter("yarns", "%".concat(yarns).concat("%"));
        }
        if (!StringUtils.isEmpty(fabric)) {
            query.setParameter("fabricDesignName", "%".concat(fabric).concat("%"));
        }

        return query.getResultList();
    }

    public List<YarnFabricDesign> findByYarnTypesAndFabricDesign(String yarnsFabricDesign) {
        if (StringUtils.isEmpty(yarnsFabricDesign)) {
            return Collections.emptyList();
        }
        /* Getting Yarns and Design from yarnFabricDesign - Start **/
        String[] yarnFabricDesignArray = yarnsFabricDesign.split("--");
        String[] yarnArr = null;
        String fabricDesign;
        boolean isAndCondition = true;
        if (yarnsFabricDesign.startsWith("--")) { //if only design entered
            fabricDesign = yarnsFabricDesign.replace("--","").trim();
        } else if (yarnFabricDesignArray.length <= 1) {
            //input param will be either of yarn or design
            isAndCondition = false;
            fabricDesign = yarnsFabricDesign.trim().toLowerCase();
            yarnArr = new String[]{fabricDesign};
        } else {
                yarnArr = yarnFabricDesignArray[0].split("x");
                fabricDesign = yarnFabricDesignArray[1].trim();
        }
        /* Getting Yarns and Design from yarnFabricDesign - End **/

        StringBuilder sb = new StringBuilder("SELECT yfd FROM YarnFabricDesign yfd ");
        if (Objects.nonNull(yarnArr)) {
            sb.append("JOIN yfd.fabricDesignYarnMappings fym JOIN fym.yarn y WHERE (");
            for (int i = 0; i < yarnArr.length; i++) {
                if (i > 0) {
                    sb.append("OR ");
                }

                sb.append("LOWER(y.type) LIKE LOWER(:yarnType").append(i).append(") ");
            }
            sb.append(")");
        }
        if (!StringUtils.isEmpty(fabricDesign)) {
            if (Objects.nonNull(yarnArr)) {
                sb.append(isAndCondition ? "AND " : "OR "); //query the input either yarn or design
            } else {
                sb.append("WHERE ");
            }

            sb.append("LOWER(yfd.fabricDesign.name) LIKE LOWER(:fabricDesign)");
        }

        TypedQuery<YarnFabricDesign> query = entityManager.createQuery(sb.toString(), YarnFabricDesign.class);

        if (Objects.nonNull(yarnArr)) {
            for (int i = 0; i < yarnArr.length; i++) {
                query.setParameter("yarnType"+i, "%"+yarnArr[i].trim()+"%");
            }
        }
        if (!StringUtils.isEmpty(fabricDesign)) {
            query.setParameter("fabricDesign", "%"+fabricDesign.trim()+"%");
        }

        return query.getResultList();
    }

    @Override
    public YarnFabricDesign save(YarnFabricDesign yarnFabricDesign) {
        return yarnFabricDesignRepo.save(yarnFabricDesign);
    }

    @Override
    public void deleteById(Long id) {
        yarnFabricDesignRepo.deleteById(id);
    }

    @Override
    public boolean existsByQualityName(String qualityName) {
        return yarnFabricDesignRepo.existsByQualityName(qualityName);
    }

    @Override
    @Transactional
    public List<FabricDesignYarnMapping> fetchFabricDesignYarnMappingById(Long id) {
        Optional<YarnFabricDesign> byId = yarnFabricDesignRepo.findById(id);
        return byId.map(YarnFabricDesign::getFabricDesignYarnMappings)
                .orElse(Collections.emptyList());
    }
}
