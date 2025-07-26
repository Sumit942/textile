package com.example.textile.serviceimpl;

import com.example.textile.dto.OrderProductMappingDto;
import com.example.textile.entity.*;
import com.example.textile.repo.MachineRepo;
import com.example.textile.repo.OrderProductMappingRepo;
import com.example.textile.repo.YarnOrderItemRepo;
import com.example.textile.service.CompanyYarnOrderProductService;
import com.example.textile.service.OrderProductMappingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.textile.transform.TransformationDTOToEntity.transformOrderProductMappingDto;
import static com.example.textile.transform.TransformationEntityToDTO.transformOrderProductMappings;
import static com.example.textile.utility.ActionValidationUtil.isNullOrLessThanOne;
import static com.example.textile.utility.LogUtils.*;

@Slf4j
@AllArgsConstructor
@Service
public class OrderProductMappingServiceImpl implements OrderProductMappingService {
    private OrderProductMappingRepo productMappingRepo;
    private MachineRepo machineRepo;
    private YarnOrderItemRepo yarnOrderItemRepo;
    private CompanyYarnOrderProductService companyYarnOrderProductService;
    private EntityManager entityManager;

    @Override
    public OrderProductMapping findById(Long id) {
        Optional<OrderProductMapping> byId = productMappingRepo.findById(id);
        return byId.orElse(null);
    }

    @Override
    public List<OrderProductMapping> findByOrdersId(Long orderId) {
        return productMappingRepo.findByOrdersId(orderId);
    }

    @Override
    public List<OrderProductMapping> saveOrderProductMapping(List<OrderProductMapping> orderProductMappings) {

        saveYarnOrderProduct(orderProductMappings);

        for (OrderProductMapping orderProductMapping : orderProductMappings) {
            Orders proxyReference = entityManager.getReference(Orders.class, orderProductMapping.getOrders().getId());
            orderProductMapping.setOrders(proxyReference);

            /*for (ProductRawMaterial rawMaterial : orderProductMapping.getRawMaterials()) {
                List<Long> yarnOrderItemIds = rawMaterial.getYarnOrderItems().stream()
                        .map(YarnOrderItem::getId).collect(Collectors.toList());
                rawMaterial.setYarnOrderItems(yarnOrderItemRepo.findAllById(yarnOrderItemIds));
            }*/
        }

        return productMappingRepo.saveAll(orderProductMappings);
    }

    private void saveYarnOrderProduct(List<OrderProductMapping> orderProductMappings) {
        for (OrderProductMapping orderProductMapping : orderProductMappings) {
            CompanyYarnOrderProduct companyYarnOrderProduct = orderProductMapping.getCompanyYarnOrderProduct();
            if (isNullOrLessThanOne(companyYarnOrderProduct.getId())) {
                List<CompanyYarnOrderProduct> saveDOrderProduct = companyYarnOrderProductService.findByYarnFabricDesignIdAndMachineId(companyYarnOrderProduct.getYarnFabricDesign().getId(),
                        companyYarnOrderProduct.getMachine().getId());

                if (CollectionUtils.isEmpty(saveDOrderProduct)) {
                    CompanyYarnOrderProduct savedOrderPrd = companyYarnOrderProductService.save(companyYarnOrderProduct);
                    orderProductMapping.setCompanyYarnOrderProduct(savedOrderPrd);
                } else {
                    orderProductMapping.setCompanyYarnOrderProduct(saveDOrderProduct.get(0));
                }
            }
        }
    }

    @Transactional
    @Override
    public OrderProductMappingDto saveOrderProductMappings(OrderProductMappingDto orderProductMappingDto) {
        String logPrefix = "saveOrderProductMappings()";
        log.info(createEntryLog(logPrefix));
        List<OrderProductMapping> productMappings = transformOrderProductMappingDto(orderProductMappingDto);
        String logSuffix = createNameValue("productMappings size", productMappings.size());

        log.info("{} {}", logPrefix, logSuffix);
        List<OrderProductMapping> savedOrderProductMapping = saveOrderProductMapping(productMappings);
        logSuffix += createNameValue("savedOrderProductMapping saved count", savedOrderProductMapping.size());
        log.info("{} {}", logPrefix, logSuffix);
        allocateQtyToYarnOrderItem(savedOrderProductMapping);


        log.info(createExitLog(logPrefix, logSuffix));
        return transformOrderProductMappings(savedOrderProductMapping);
    }

    private void allocateQtyToYarnOrderItem(List<OrderProductMapping> savedOrderProductMapping) {
        String logPrefix = "setAllocatedQtyToYarnOrderItem()";
        log.info(createEntryLog(logPrefix));
        StringBuilder logSuffix = new StringBuilder(createNameValue("savedOrderProductMapping size", savedOrderProductMapping.size()));

        for (OrderProductMapping orderProductMapping : savedOrderProductMapping) {
            List<ProductRawMaterial> rawMaterials = orderProductMapping.getRawMaterials();

            for (ProductRawMaterial rawMaterial : rawMaterials) {
                //qty to be allocation to yarnOrderItems
                BigDecimal allocationQty = orderProductMapping.getQuantity().multiply(rawMaterial.getPercentage());
                logSuffix.append(createNameValue("allocationQty", allocationQty));
                log.info("{} {}", logPrefix, logSuffix);
                //allocating the quantity from yarnOrderItem for yarnOrderProduct
//                List<YarnOrderItem> yarnOrderItems = rawMaterial.getYarnOrderItems();
                List<ProductRawMaterialYarnOrderItem> productRawMaterialYarnOrderItems = rawMaterial.getYarnOrderItems();
                for (ProductRawMaterialYarnOrderItem productRawMaterialYarnOrderItem : productRawMaterialYarnOrderItems) {
                    YarnOrderItem yarnOrderItem = productRawMaterialYarnOrderItem.getYarnOrderItem();
                    logSuffix.append(createNameValue("yarnOrderItem Id", yarnOrderItem.getId()));
                    logSuffix.append(createNameValue("QtyLeft", yarnOrderItem.getQtyLeft()));
                    logSuffix.append(createNameValue("Qty to be Allocated", allocationQty));

                    if (yarnOrderItem.getQtyLeft().compareTo(allocationQty) >= 0) {
                        yarnOrderItem.setQtyAllocated(yarnOrderItem.getQtyAllocated().add(allocationQty));
                        break;
                    } else {
                        allocationQty = allocationQty.subtract(yarnOrderItem.getQtyLeft());
                        yarnOrderItem.setQtyAllocated(yarnOrderItem.getQtyAllocated().add(yarnOrderItem.getQtyLeft()));
                    }
                    YarnOrderItem saveOrderItem = yarnOrderItemRepo.save(yarnOrderItem);
                    productRawMaterialYarnOrderItem.setYarnOrderItem(saveOrderItem);
                }
                log.info("{} saving yarnOrderItems", logPrefix);
            }

        }

        log.info(createExitLog(logPrefix, logSuffix.toString()));
    }

    @Override
    public List<Machine> findMachineByMachineNo(String machineNo) {
        return machineRepo.findByMachineNo(machineNo);
    }

    @Override
    public Machine findMachineById(Long id) {
        return machineRepo.findById(id).orElse(null);
    }
}
