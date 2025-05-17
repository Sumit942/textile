package com.example.textile.controller;

import com.example.textile.dto.YarnOrderItemDto;
import com.example.textile.service.YarnOrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
            @RequestParam(defaultValue = "false") Boolean isUsed) {
        List<YarnOrderItemDto> yarnOrderItemDtos = orderItemService.findAllByCompanyIdAndIsUsed(companyId, isUsed);
        return ResponseEntity.ok(yarnOrderItemDtos);
    }
}
