package com.example.textile.service;

import com.example.textile.dto.YarnOrderItemDto;

import java.util.List;

public interface YarnOrderItemService {
    List<YarnOrderItemDto> findAllByCompanyIdAndIsUsed(Long companyId, Boolean isUsed);

    List<YarnOrderItemDto> findAllByCompanyIdAndYarnIdAndIsUsed(Long companyId, Long yarnId, Boolean isUsed);
}
