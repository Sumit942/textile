package com.example.textile.transform;

import com.example.textile.dto.CompanyDto;
import com.example.textile.dto.CompanyYarnOrderDto;
import com.example.textile.dto.OrderProductMappingDto;
import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.*;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

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
        orders.setOrderNo(orders.getOrderNo());
        if (Objects.nonNull(ordersDto.getCompany()) && Objects.nonNull(ordersDto.getCompany().getId())) {
            orders.setCompany(modelMapper.map(ordersDto.getCompany(), Company.class));
        }

        if (!isEmpty(ordersDto.getCompanyYarnOrders())) {
            ordersDto.getCompanyYarnOrders().stream()
                    .map(TransformationDTOToEntity::transformCompanyYarnOrder)
                    .forEach(orders::addCompanyYarnOrders);
        }
        return orders;
    }

    public static Company transformCompany(CompanyDto companyDto) {
        if (Objects.isNull(companyDto)) {
            return null;
        }
        Company company = new Company();
        company.setId(companyDto.getId());
        company.setName(companyDto.getName());
        company.setCode(companyDto.getCode());
        return company;
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
        if (companyYarnOrder.getYarnBuilties() != null) {
            companyYarnOrder.getYarnBuilties()
                    .forEach(TransformationDTOToEntity::validateYarnBuilty);
        }

        companyYarnOrder.setSupplier(transformCompany(companyYarnOrderDto.getSupplier()));
        companyYarnOrder.setIGst(companyYarnOrderDto.getIGst());
        companyYarnOrder.setCGst(companyYarnOrderDto.getCGst());
        companyYarnOrder.setSGst(companyYarnOrderDto.getSGst());
        companyYarnOrder.setDiscPerc(companyYarnOrderDto.getDiscPerc());
        return companyYarnOrder;
    }

    public static void validateYarnBuilty(YarnBuilty yarnBuilty) {
        Company tranportCompany = yarnBuilty.getTranportCompany();
        if (Objects.isNull(tranportCompany) || Objects.isNull(tranportCompany.getId())) {
            yarnBuilty.setTranportCompany(null);
        }
    }

    public static List<OrderProductMapping> transformOrderProductMappingDto(OrderProductMappingDto productMappingDto) {
        if (Objects.isNull(productMappingDto)) {
            return Collections.emptyList();
        }
        List<OrderProductMapping> productMappings = new ArrayList<>();
        if (!isEmpty(productMappingDto.getOrderProducts())) {
            for (OrderProductMappingDto.OrderProduct orderProduct : productMappingDto.getOrderProducts()) {
                OrderProductMapping productMapping = new OrderProductMapping();

                productMapping.setId(orderProduct.getId());
                productMapping.setOrders(transformOrdersDto(null,productMappingDto.getOrders()));
                productMapping.setQuantity(orderProduct.getQuantity());
                productMapping.setCompanyYarnOrderProduct(orderProduct.getCompanyYarnOrderProduct());
                productMapping.setRawMaterials(orderProduct.getRawMaterial());
            }
        }

        return productMappings;
    }
}
