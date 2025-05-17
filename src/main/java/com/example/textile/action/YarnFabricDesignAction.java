package com.example.textile.action;

import com.example.textile.entity.FabricDesign;
import com.example.textile.entity.FabricDesignYarnMapping;
import com.example.textile.entity.YarnFabricDesign;
import com.example.textile.enums.ResponseType;
import com.example.textile.executors.ActionResponse;
import com.example.textile.executors.RestActionExecutor;
import com.example.textile.service.YarnFabricDesignService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.example.textile.utility.ActionValidationUtil.isNullOrLessThanOne;
import static com.example.textile.utility.LogUtils.*;

@AllArgsConstructor
@Slf4j
public class YarnFabricDesignAction extends RestActionExecutor<YarnFabricDesign> {
    private YarnFabricDesignService yarnFabricDesignService;

    @Override
    protected ActionResponse<YarnFabricDesign> onSuccessRest(YarnFabricDesign yarnFabricDesign, Map<String, Object> parameterMap) {
        String logPrefix = "onSuccessRest()";
        String logSuffix = createNameValue("yarnFabricDesign id", yarnFabricDesign.getId());
        log.info(createEntryLog(logPrefix));

        YarnFabricDesign save = yarnFabricDesignService.save(yarnFabricDesign);
        ActionResponse<YarnFabricDesign> actionResponse = new ActionResponse<>(ResponseType.SUCCESS);
        actionResponse.setDbObj(save);

        log.info(createExitLog(logPrefix, logSuffix));
        return actionResponse;
    }

    @Override
    protected void doValidationRest(YarnFabricDesign yarnFabricDesign, Map<String, Object> parameterMap, Map<String, String[]> errorMap) {
        String logPrefix = "doValidationRest()";
        String logSuffix = createNameValue("yarnFabricDesign id", yarnFabricDesign.getId());
        log.info(createEntryLog(logPrefix));

        FabricDesign fabricDesign = yarnFabricDesign.getFabricDesign();
        if (Objects.isNull(fabricDesign)) {
            errorMap.put("fabricDesign.id", new String[]{"NotNull.yarnFabricDesign.fabricDesign"});
        } else if (isNullOrLessThanOne(fabricDesign.getId())) {
            errorMap.put("fabricDesign.id", new String[]{"NotNull.yarnFabricDesign.fabricDesign.id"});
        } else if (StringUtils.isBlank(fabricDesign.getName())) {
            errorMap.put("fabricDesign.name", new String[]{"NotNull.yarnFabricDesign.fabricDesign.name"});
        }

        List<FabricDesignYarnMapping> fabricDesignYarnMappings = yarnFabricDesign.getFabricDesignYarnMappings();
        if (CollectionUtils.isEmpty(fabricDesignYarnMappings)) {
            errorMap.put("fabricDesignYarnMappings", new String[]{"NotNull.yarnFabricDesign.fabricDesignYarnMappings"});
        }

        List<Long> yarnIds = new ArrayList<>();
        for (int i = 0; i < fabricDesignYarnMappings.size(); i++) {
            FabricDesignYarnMapping next = fabricDesignYarnMappings.get(i);
            if (Objects.isNull(next.getYarn()) || isNullOrLessThanOne(next.getYarn().getId())) {
                errorMap.put("fabricDesignYarnMappings["+i+"].yarn.id", new String[]{"NotNull.yarnFabricDesign.fabricDesignYarnMappings.yarn.id"});
                if (yarnIds.contains(next.getYarn().getId())) {
                    errorMap.put("fabricDesignYarnMappings["+i+"].yarn.id", new String[]{"Duplicate.yarnFabricDesign.fabricDesignYarnMappings.yarn.id"});
                } else {
                    yarnIds.add(next.getYarn().getId());
                }
            }
        }

        validateDbRecord(yarnFabricDesign, errorMap);

        log.info(createExitLog(logPrefix, logSuffix));
    }

    private void validateDbRecord(YarnFabricDesign yarnFabricDesign, Map<String, String[]> errorMap) {
        if (errorMap.isEmpty()) {
            yarnFabricDesign.perPersist();
            if (!StringUtils.isBlank(yarnFabricDesign.getQualityName())) {
                boolean existsByQualityName = yarnFabricDesignService.existsByQualityName(yarnFabricDesign.getQualityName());
                if (existsByQualityName) {
                    if (!isNullOrLessThanOne(yarnFabricDesign.getId())) {
                        Long id = yarnFabricDesignService.getYarnFabricDesignIdByQualityName(yarnFabricDesign.getQualityName());
                        if (id.compareTo(yarnFabricDesign.getId()) != 0) {
                            errorMap.put("yarnFabricDesign", new String[]{"Exists.yarnFabricDesign"});
                        }
                    } else {
                        errorMap.put("yarnFabricDesign", new String[]{"Exists.yarnFabricDesign"});
                    }
                }
            }
        }
    }

    @Override
    protected void doPreSaveOperationRest(YarnFabricDesign yarnFabricDesign, Map<String, Object> parameterMap, Map<String, String[]> errorMap) {
        // do nothing
    }
}
