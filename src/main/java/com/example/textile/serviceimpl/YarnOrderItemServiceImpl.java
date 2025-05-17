package com.example.textile.serviceimpl;

import com.example.textile.dto.YarnOrderItemDto;
import com.example.textile.entity.YarnOrderItem;
import com.example.textile.repo.YarnOrderItemRepo;
import com.example.textile.service.YarnOrderItemService;
import com.example.textile.transform.TransformationEntityToDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        yarnOrderItems = orderItemRepo.findByCompanyYarnOrderOrderCompanyIdAndQtyUsedGreaterThan(companyId, 0);
        return yarnOrderItems.stream()
                .map(TransformationEntityToDTO::transformYarOrderItem)
                .collect(Collectors.toList());
    }
}
