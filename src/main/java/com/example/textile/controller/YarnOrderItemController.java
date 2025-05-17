package com.example.textile.controller;

import com.example.textile.dto.YarnOrderItemDto;
import com.example.textile.service.YarnOrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.textile.utility.LogUtils.*;

@Slf4j
@RestController
@RequestMapping("yarnOrderItem")
public class YarnOrderItemController {

    private final YarnOrderItemService orderItemService;

    public YarnOrderItemController(YarnOrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public ResponseEntity<List<YarnOrderItemDto>> findAll(
            @RequestParam Long companyId,
            @RequestParam Long yarnId,
            @RequestParam(defaultValue = "false") Boolean isUsed) {
        String logPrefix = "findAll()";
        log.info(createEntryLog(logPrefix));
        String logSuffix = createNameValue("companyId", companyId) + createNameValue("yarnId", yarnId) + createNameValue("isUsed",isUsed);

        log.info("{} {}",logPrefix, logSuffix);
        List<YarnOrderItemDto> yarnOrderItemDtos = orderItemService.findAllByCompanyIdAndYarnIdAndIsUsed(companyId, yarnId, isUsed);
        log.info(createExitLog(logPrefix, logSuffix));

        return ResponseEntity.ok(yarnOrderItemDtos);
    }
}
