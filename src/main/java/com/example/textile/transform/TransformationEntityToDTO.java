package com.example.textile.transform;

import com.example.textile.dto.*;
import com.example.textile.entity.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

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
            ordersDto.setOrderNo(orders.getOrderNo());
            if (Objects.nonNull(orders.getCompany())) {
                ordersDto.setCompany(transformCompanyEntity(orders.getCompany()));
            }

            if (!isEmpty(orders.getCompanyYarnOrders())) {
                List<CompanyYarnOrderDto> yarnOrderDtoList = orders.getCompanyYarnOrders().stream()
                        .map(TransformationEntityToDTO::transformCompanyYarnOrderEntity)
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

    private static CompanyDto transformCompanyEntity(Company company) {
        if (Objects.isNull(company)) {
            return null;
        }
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(company.getId());
        companyDto.setName(company.getName());
        companyDto.setCode(company.getCode());
        return companyDto;
    }

    public static CompanyYarnOrderDto transformCompanyYarnOrderEntity(CompanyYarnOrder yarnOrder) {
        CompanyYarnOrderDto companyYarnOrderDto = new CompanyYarnOrderDto();

        if (yarnOrder.getOrder() != null) {
            companyYarnOrderDto.setOrderId(yarnOrder.getOrder().getId());
            companyYarnOrderDto.setOrderNo(yarnOrder.getOrder().getOrderNo());
        }
        companyYarnOrderDto.setVersion(yarnOrder.getVersion());
        companyYarnOrderDto.setId(yarnOrder.getId());
        companyYarnOrderDto.setRemark(yarnOrder.getRemark());
        companyYarnOrderDto.setYarnInvoiceNo(yarnOrder.getYarnInvoiceNo());
        companyYarnOrderDto.setOrderDt(yarnOrder.getOrderDt());
        companyYarnOrderDto.setTotalQuantity(yarnOrder.getTotalQuantity());
        companyYarnOrderDto.setTotalAmount(yarnOrder.getTotalAmount());
        companyYarnOrderDto.setYarnOrderItems(yarnOrder.getYarnOrderItems());
        companyYarnOrderDto.setYarnBuilties(yarnOrder.getYarnBuilties());
        companyYarnOrderDto.setSupplier(transformCompanyEntity(yarnOrder.getSupplier()));
        companyYarnOrderDto.setIGst(yarnOrder.getIGst());
        companyYarnOrderDto.setCGst(yarnOrder.getCGst());
        companyYarnOrderDto.setSGst(yarnOrder.getSGst());
        companyYarnOrderDto.setDiscPerc(yarnOrder.getDiscPerc());

        return companyYarnOrderDto;
    }

    public static YarnFabricDesignDto transformYarnFabricDesignEntity(YarnFabricDesign yarnFabricDesign) {
        YarnFabricDesignDto yarnFabricDesignDto = new YarnFabricDesignDto();
        yarnFabricDesignDto.setId(yarnFabricDesign.getId());
        yarnFabricDesignDto.setFabricDesign(yarnFabricDesign.getFabricDesign());
        yarnFabricDesignDto.setGsm(yarnFabricDesign.getGsm());
        yarnFabricDesignDto.setQualityName(yarnFabricDesign.getQualityName());
        return yarnFabricDesignDto;
    }

    public static OrderProductMappingDto transformOrderProductMappings(List<OrderProductMapping> savedOrderProductMapping) {
        OrderProductMappingDto productMappingDto = new OrderProductMappingDto();

        if (!isEmpty(savedOrderProductMapping)) {
            Orders orders = savedOrderProductMapping.get(0).getOrders();
            OrdersDto ordersDto = new OrdersDto();
            ordersDto.setId(orders.getId());
            ordersDto.setOrderNo(orders.getOrderNo());
            productMappingDto.setOrders(ordersDto);

            List<OrderProductMappingDto.OrderProduct> orderProducts = productMappingDto.getOrderProducts();
            for (OrderProductMapping productMapping : savedOrderProductMapping) {
                OrderProductMappingDto.OrderProduct orderProduct = new OrderProductMappingDto.OrderProduct();
                orderProduct.setCompanyYarnOrderProduct(productMapping.getCompanyYarnOrderProduct());
                orderProduct.setQuantity(productMapping.getQuantity());
                orderProduct.setRawMaterials(productMapping.getRawMaterials());
                orderProduct.setQuantity(productMapping.getQuantity());

                orderProducts.add(orderProduct);
            }
        }

        return productMappingDto;
    }

    public static YarnOrderItemDto transformYarOrderItem(YarnOrderItem yarnOrderItem) {
        YarnOrderItemDto yarnOrderItemDto = new YarnOrderItemDto();

        yarnOrderItemDto.setId(yarnOrderItem.getId());
        yarnOrderItemDto.setBoxes(yarnOrderItem.getBoxes());
        yarnOrderItemDto.setYarn(yarnOrderItem.getYarn());
        yarnOrderItemDto.setQuantity(yarnOrderItem.getQuantity());
        yarnOrderItemDto.setQtyAllocated(yarnOrderItem.getQtyAllocated());
        yarnOrderItemDto.setQtyLeft(yarnOrderItem.getQtyLeft());
        CompanyYarnOrder companyYarnOrder = yarnOrderItem.getCompanyYarnOrder();
        if (Objects.nonNull(companyYarnOrder) && Objects.nonNull(companyYarnOrder.getOrder())
                && Objects.nonNull(companyYarnOrder.getOrder().getCompany())) {
            yarnOrderItemDto.setCompanyId(companyYarnOrder.getOrder().getCompany().getId());
            yarnOrderItemDto.setCompanyName(companyYarnOrder.getOrder().getCompany().getName());
            yarnOrderItemDto.setOrderNo(companyYarnOrder.getOrder().getOrderNo());
        }

        return yarnOrderItemDto;
    }
}
