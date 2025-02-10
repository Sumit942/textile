package com.example.textile.transform;

import com.example.textile.dto.CompanyYarnOrderDto;
import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.Company;
import com.example.textile.entity.CompanyYarnOrder;
import com.example.textile.entity.Orders;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class TransformationDTOToEntity {

    private static final Logger log = LoggerFactory.getLogger(TransformationDTOToEntity.class);

    public static Orders transformOrdersDto(@NotNull ModelMapper modelMapper, OrdersDto ordersDto) {
        String logPrefix = "transformOrdersEntity() ";
        log.debug("{}Entry {}", logPrefix, ordersDto.getId());
        Orders orders = new Orders();
        orders.setVersion(ordersDto.getVersion());
        orders.setId(ordersDto.getId());
        orders.setRemarks(ordersDto.getRemarks());
        orders.setOrderStatusType(ordersDto.getOrderStatusType());
        if (Objects.nonNull(ordersDto.getCompany())) {
            orders.setCompany(modelMapper.map(ordersDto.getCompany(), Company.class));
        }

        if (!CollectionUtils.isEmpty(ordersDto.getCompanyYarnOrders())) {
            ordersDto.getCompanyYarnOrders().stream()
                    .map(TransformationDTOToEntity::transformCompanyYarnOrder)
                    .forEach(orders::addCompanyYarnOrders);
        }
        return orders;
    }

    public static CompanyYarnOrder transformCompanyYarnOrder(CompanyYarnOrderDto companyYarnOrderDto) {
        CompanyYarnOrder companyYarnOrder = new CompanyYarnOrder();

        companyYarnOrder.setVersion(companyYarnOrderDto.getVersion());
        companyYarnOrder.setId(companyYarnOrderDto.getId());
        companyYarnOrder.setRemark(companyYarnOrderDto.getRemark());
        companyYarnOrder.setYarnInvoiceNo(companyYarnOrderDto.getYarnInvoiceNo());
        companyYarnOrder.setOrderDt(companyYarnOrderDto.getOrderDt());
        companyYarnOrder.setTotalQuantity(companyYarnOrderDto.getTotalQuantity());
        companyYarnOrder.setTotalAmount(companyYarnOrderDto.getTotalAmount());
        companyYarnOrder.setYarnOrderItems(companyYarnOrderDto.getYarnOrderItems());
        companyYarnOrder.setYarnBuilties(companyYarnOrderDto.getYarnBuilties());

        return companyYarnOrder;
    }
}
