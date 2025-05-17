package com.example.textile.serviceimpl;

import com.example.textile.dto.YarnOrderItemDto;
import com.example.textile.entity.YarnOrderItem;
import com.example.textile.repo.YarnOrderItemRepo;
import com.example.textile.service.YarnOrderItemService;
import com.example.textile.transform.TransformationEntityToDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.textile.utility.LogUtils.*;

@Slf4j
@Service
public class YarnOrderItemServiceImpl implements YarnOrderItemService {

    @Autowired
    YarnOrderItemRepo orderItemRepo;

    @Override
    public List<YarnOrderItemDto> findAllByCompanyIdAndIsUsed(Long companyId, Boolean isUsed) {
        List<YarnOrderItem> yarnOrderItems;
        if (!isUsed) {
            yarnOrderItems = orderItemRepo.findByCompanyYarnOrderOrderCompanyId(companyId);
            return yarnOrderItems.stream()
                    .map(TransformationEntityToDTO::transformYarOrderItem)
                    .collect(Collectors.toList());
        }
        yarnOrderItems = orderItemRepo.findByCompanyYarnOrderOrderCompanyIdAndQtyLeftGreaterThan(companyId, BigDecimal.ZERO);
        return yarnOrderItems.stream()
                .map(TransformationEntityToDTO::transformYarOrderItem)
                .collect(Collectors.toList());
    }

    @Override
    public List<YarnOrderItemDto> findAllByCompanyIdAndYarnIdAndIsUsed(Long companyId, Long yarnId, Boolean isUsed) {
        String logPrefix = "findAllByCompanyIdAndYarnIdAndIsUsed()";
        log.info(createEntryLog(logPrefix));
        String logSuffix = createNameValue("companyId", companyId);

        List<YarnOrderItem> yarnOrderItems;
        if (!isUsed) {
            yarnOrderItems = orderItemRepo.findByCompanyYarnOrderOrderCompanyIdAndYarnId(companyId, yarnId);
            logSuffix += createNameValue("yarnOrderItems size", yarnOrderItems.size());
            log.info(createExitLog(logPrefix, logSuffix));
            return yarnOrderItems.stream()
                    .map(TransformationEntityToDTO::transformYarOrderItem)
                    .collect(Collectors.toList());
        }
        yarnOrderItems = orderItemRepo.findByCompanyYarnOrderOrderCompanyIdAndYarnIdAndQtyLeftGreaterThan(companyId, yarnId, BigDecimal.ZERO);
        logSuffix += createNameValue("yarnOrderItems size", yarnOrderItems.size());
        log.info(createExitLog(logPrefix, logSuffix));
        return yarnOrderItems.stream()
                .map(TransformationEntityToDTO::transformYarOrderItem)
                .collect(Collectors.toList());

    }
}
