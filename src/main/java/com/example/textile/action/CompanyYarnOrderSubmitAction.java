package com.example.textile.action;

import com.example.textile.dto.CompanyYarnOrderDto;
import com.example.textile.entity.CompanyYarnOrder;
import com.example.textile.entity.YarnOrderItem;
import com.example.textile.enums.ResponseType;
import com.example.textile.executors.ActionResponse;
import com.example.textile.executors.RestActionExecutor;
import com.example.textile.service.CompanyYarnOrderService;
import com.example.textile.service.YarnService;
import com.example.textile.transform.TransformationEntityToDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

import static com.example.textile.utility.LogUtils.*;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@AllArgsConstructor
@Slf4j
public class CompanyYarnOrderSubmitAction extends RestActionExecutor<CompanyYarnOrderDto> {
    private CompanyYarnOrderService yarnOrderService;
    private YarnService yarnService;

    @Override
    protected ActionResponse<CompanyYarnOrderDto> onSuccessRest(CompanyYarnOrderDto companyYarnOrderDto, Map<String, Object> parameterMap) {
        String logPrefix = "onSuccessRest()";
        String logSuffix = createLogSuffix("companyYarnOrderDtoId", companyYarnOrderDto.getId());
        log.info(createEntryLog(logPrefix));

        CompanyYarnOrder save = yarnOrderService.save(companyYarnOrderDto);
        CompanyYarnOrderDto yarnOrderDto = TransformationEntityToDTO.transformCompanyYarnOrderDto(save);

        ActionResponse<CompanyYarnOrderDto> actionResponse = new ActionResponse<>(ResponseType.SUCCESS);
        actionResponse.setDbObj(yarnOrderDto);

        log.info(createExitLog(logPrefix, logSuffix));
        return actionResponse;
    }

    @Override
    protected void doValidationRest(CompanyYarnOrderDto companyYarnOrderDto, Map<String, Object> parameterMap, Map<String, String[]> errorMap) {
        String logPrefix = "doValidationRest()";
        log.info(createEntryLog(logPrefix));
        String logSuffix = createLogSuffix("companyYarnOrderDto id", companyYarnOrderDto.getId());
        if (!isEmpty(companyYarnOrderDto.getYarnOrderItems())) {
            errorMap.put("yarnOrderItems", new String[]{"IsEmpty.companyYarnOrderDto.yarnOrderItems"});
        } else {
            for (int i = 0; i < companyYarnOrderDto.getYarnOrderItems().size(); i++) {
                YarnOrderItem yarnOrderItem = companyYarnOrderDto.getYarnOrderItems().get(i);
                if (Objects.isNull(yarnOrderItem.getYarn()) || Objects.isNull(yarnOrderItem.getYarn().getId())) {
                    errorMap.put("yarnOrderItems["+i+"].yarn.name", new String[]{"NotNull.yarnOrderItems.yarn"});
                }
                if (Objects.isNull(yarnOrderItem.getBoxes()) || yarnOrderItem.getBoxes().compareTo(0) <= 0) {
                    errorMap.put("yarnOrderItems["+i+"].boxes", new String[]{"NotNull.yarnOrderItems.boxes"});
                }
                if (Objects.isNull(yarnOrderItem.getQuantity()) || yarnOrderItem.getQuantity().compareTo(0.00) <= 0) {
                    errorMap.put("yarnOrderItems["+i+"].quantity", new String[]{"NotNull.yarnOrderItems.quantity"});
                }
            }
        }
        logSuffix += createLogSuffix("errorCount",errorMap.size());
        log.info(createExitLog(logPrefix, logSuffix));
    }

    @Override
    protected void doPreSaveOperationRest(CompanyYarnOrderDto companyYarnOrderDto, Map<String, Object> parameterMap, Map<String, String[]> errorMap) {

    }
}
