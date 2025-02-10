package com.example.textile.transform;

import com.example.textile.dto.CompanyDto;
import com.example.textile.dto.CompanyYarnOrderDto;
import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.CompanyYarnOrder;
import com.example.textile.entity.Orders;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransformationEntityToDTO {

    private static final Logger log = LoggerFactory.getLogger(TransformationEntityToDTO.class);

    public static OrdersDto transformOrdersEntity(@NotNull ModelMapper modelMapper, Orders orders) {
        String logPrefix = "transformOrdersEntity() ";
        log.debug("{}Entry {}", logPrefix, orders.getId());
        OrdersDto ordersDto = new OrdersDto();
        try {
            ordersDto.setVersion(orders.getVersion());
            ordersDto.setId(orders.getId());
            ordersDto.setRemarks(orders.getRemarks());
            ordersDto.setOrderStatusType(orders.getOrderStatusType());
            if (Objects.nonNull(orders.getCompany())) {
                ordersDto.setCompany(modelMapper.map(orders.getCompany(), CompanyDto.class));
            }

            if (!CollectionUtils.isEmpty(orders.getCompanyYarnOrders())) {
                List<CompanyYarnOrderDto> yarnOrderDtoList = orders.getCompanyYarnOrders().stream()
                        .map(TransformationEntityToDTO::transformCompanyYarnOrderDto)
                        .collect(Collectors.toList());
                ordersDto.setCompanyYarnOrders(yarnOrderDtoList);
            }
            log.debug("{} Exit[OrderID={}]", logPrefix, orders.getId());
            return ordersDto;
        } catch (Throwable t) {
            log.error(logPrefix + "Exception while tranforming[Order->dto]:{}", orders.getId(), t);
            return ordersDto;
        }
    }

    public static CompanyYarnOrderDto transformCompanyYarnOrderDto(CompanyYarnOrder yarnOrder) {
        CompanyYarnOrderDto companyYarnOrderDto = new CompanyYarnOrderDto();

        companyYarnOrderDto.setVersion(yarnOrder.getVersion());
        companyYarnOrderDto.setId(yarnOrder.getId());
        companyYarnOrderDto.setRemark(yarnOrder.getRemark());
        companyYarnOrderDto.setYarnInvoiceNo(yarnOrder.getYarnInvoiceNo());
        companyYarnOrderDto.setOrderDt(yarnOrder.getOrderDt());
        companyYarnOrderDto.setTotalQuantity(yarnOrder.getTotalQuantity());
        companyYarnOrderDto.setTotalAmount(yarnOrder.getTotalAmount());
        companyYarnOrderDto.setYarnOrderItems(yarnOrder.getYarnOrderItems());
        companyYarnOrderDto.setYarnBuilties(yarnOrder.getYarnBuilties());

        return companyYarnOrderDto;
    }
}
