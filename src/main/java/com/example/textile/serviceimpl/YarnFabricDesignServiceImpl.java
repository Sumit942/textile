package com.example.textile.serviceimpl;

import com.example.textile.entity.YarnFabricDesign;
import com.example.textile.service.YarnFabricDesignService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class YarnFabricDesignServiceImpl implements YarnFabricDesignService {
    private EntityManager entityManager;

    @Override
    public List<YarnFabricDesign> findByYarnTypesAndFabricDesign(String yarnsFabricDesign) {
        if (StringUtils.isEmpty(yarnsFabricDesign)) {
            return Collections.emptyList();
        }
        /* Getting Yarns and Design from yarnFabricDesign - Start **/
        String[] yarnFabricDesignArray = yarnsFabricDesign.split("-");
        String[] yarnArr = null;
        String fabricDesign;
        if (yarnsFabricDesign.startsWith("-")) { //if only design entered
            fabricDesign = yarnsFabricDesign.replace("-","").trim();
        } else if (yarnFabricDesignArray.length <= 1) {
            //input param will be either of yarn or design
            fabricDesign = yarnsFabricDesign.trim().toLowerCase();
            yarnArr = new String[]{fabricDesign};
        } else {
                yarnArr = yarnFabricDesignArray[0].split("x");
                fabricDesign = yarnFabricDesignArray[1].trim();
        }
        /* Getting Yarns and Design from yarnFabricDesign - End **/

        StringBuilder sb = new StringBuilder("SELECT yfd FROM YarnFabricDesign yfd ");
        if (Objects.nonNull(yarnArr)) {
            sb.append("JOIN yfd.yarns yfm JOIN yfm.yarn y WHERE ");
            sb.append("(");
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
                sb.append("AND ");
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
}
