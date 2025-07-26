package com.example.textile.action;

import com.example.textile.dto.OrderProductMappingDto;
import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.CompanyYarnOrderProduct;
import com.example.textile.entity.Machine;
import com.example.textile.entity.ProductRawMaterial;
import com.example.textile.entity.YarnOrderItem;
import com.example.textile.enums.ResponseType;
import com.example.textile.executors.ActionResponse;
import com.example.textile.executors.RestActionExecutor;
import com.example.textile.service.OrderProductMappingService;
import com.example.textile.utility.ActionValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.textile.utility.ActionValidationUtil.isEqualToLessThanZero;
import static com.example.textile.utility.ActionValidationUtil.isNullOrLessThanOne;
import static com.example.textile.utility.LogUtils.*;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@AllArgsConstructor
@Slf4j
public class OrderProductMappingAction extends RestActionExecutor<OrderProductMappingDto> {
    private OrderProductMappingService productMappingService;

    @Override
    protected ActionResponse<OrderProductMappingDto> onSuccessRest(OrderProductMappingDto orderProductMappingDto, Map<String, Object> parameterMap) {
        String logPrefix = "onSuccessRest()";
        log.info(createEntryLog(logPrefix));
        String logSuffix = createNameValue("orderProductMappingDto orderNo", orderProductMappingDto.getOrders().getOrderNo());

        OrderProductMappingDto orderProductMappingDtoSaved = productMappingService
                .saveOrderProductMappings(orderProductMappingDto);

        ActionResponse<OrderProductMappingDto> actionResponse = new ActionResponse<>(ResponseType.SUCCESS);
        actionResponse.setDbObj(orderProductMappingDtoSaved);

        log.info(createExitLog(logPrefix, logSuffix));
        return actionResponse;
    }

    @Override
    protected void doValidationRest(OrderProductMappingDto orderProductMappingDto, Map<String, Object> parameterMap, Map<String, String[]> errorMap) {
        String logPrefix = "doValidationRest()";
        log.info(createEntryLog(logPrefix));
        String logSuffix = "";

        OrdersDto orders = orderProductMappingDto.getOrders();
        if (Objects.isNull(orders) || StringUtils.isEmpty(orders.getOrderNo()) || isNullOrLessThanOne(orders.getId())) {
            errorMap.put("orders",new String[]{"NotNull.orderProductMappingDto.orders"});
            return;
        }
        log.info("{} {}",logPrefix,createNameValue("orderNo", orders.getOrderNo()));
        List<OrderProductMappingDto.OrderProduct> orderProducts = orderProductMappingDto.getOrderProducts();
        if(isEmpty(orderProducts)) {
            errorMap.put("orderProducts",new String[]{"NotNull.orderProductMappingDto.orderProducts"});
            return;
        }
        for (int i = 0; i < orderProducts.size(); i++) {
            OrderProductMappingDto.OrderProduct orderProduct = orderProducts.get(i);
            CompanyYarnOrderProduct yarnOrderProduct = orderProduct.getCompanyYarnOrderProduct();

            if (isNullOrLessThanOne(yarnOrderProduct.getYarnFabricDesign().getId())){
                errorMap.put(String.format("orderProducts[%s].companyYarnOrderProduct.yarnFabricDesign",i),
                        new String[]{"NotNull.orderProductMappingDto.orderProducts.companyYarnOrderProduct.yarnFabricDesign"});
            }
            if (isNullOrLessThanOne(yarnOrderProduct.getMachine().getId()) && StringUtils.isEmpty(yarnOrderProduct.getMachine().getMachineNo())) {
                errorMap.put(String.format("orderProducts[%s].machine",i),
                        new String[]{"NotNull.orderProductMappingDto.orderProducts.companyYarnOrderProduct.machine"});
            }
            if (ActionValidationUtil.isEqualToLessThanZero(orderProduct.getQuantity())) {
                errorMap.put(String.format("orderProducts[%s].quantity",i),
                        new String[]{"NotNull.orderProductMappingDto.orderProducts.quantity"});
            }

            List<ProductRawMaterial> rawMaterials = orderProduct.getRawMaterials();
            if (isEmpty(rawMaterials)) {
                errorMap.put(String.format("orderProducts[%s].rawMaterials",i),
                        new String[]{"NotNull.orderProductMappingDto.orderProducts.rawMaterial"});
                continue;
            }

            for (int j = 0; j < rawMaterials.size(); j++) {

                ProductRawMaterial rawMaterial = rawMaterials.get(j);
                if (Objects.isNull(rawMaterial.getPercentage()) || isEqualToLessThanZero(rawMaterial.getPercentage())) {
                    errorMap.put(String.format("orderProducts[%s].rawMaterials[%s].percentage",i,j),
                            new String[]{"NotNull.orderProductMappingDto.rawMaterial.percentage"});
                }
                if (isEmpty(rawMaterial.getYarnOrderItems())) {
                    errorMap.put(String.format("orderProducts[%s].rawMaterials[%s].yarnOrderItem",i,j),
                            new String[]{"NotNull.orderProductMappingDto.orderProducts.rawMaterial.yarnOrderItem"});
                } else {
                    for (int k = 0; k < rawMaterial.getYarnOrderItems().size(); k++) {
                        YarnOrderItem yarnOrderItem = rawMaterial.getYarnOrderItems().get(k).getYarnOrderItem();
                        if (isNullOrLessThanOne(yarnOrderItem.getId())) {
                            errorMap.put(String.format("orderProducts[%s].rawMaterials[%s].yarnOrderItem[%s].id",i,j,k),
                                    new String[]{"NotNull.orderProductMappingDto.orderProducts.rawMaterial.yarnOrderItem"});
                        }
                    }
                }
            }
        }

        logSuffix += createNameValue("errorMap siz", errorMap.size());
        log.info(createExitLog(logPrefix, logSuffix));
    }

    @Override
    protected void doPreSaveOperationRest(OrderProductMappingDto orderProductMappingDto, Map<String, Object> parameterMap, Map<String, String[]> errorMap) {
        String logPrefix = "doPreSaveOperationRest()";
        log.info(createEntryLog(logPrefix));
        String logSuffix = createNameValue("orderProductMappingDto orderNo", orderProductMappingDto.getOrders().getOrderNo());

        List<OrderProductMappingDto.OrderProduct> orderProducts = orderProductMappingDto.getOrderProducts();
        for (int i = 0; i < orderProducts.size(); i++) {
            Machine machine = orderProducts.get(i).getCompanyYarnOrderProduct().getMachine();

            if (!isNullOrLessThanOne(machine.getId())) {
                Machine existingMcn = productMappingService.findMachineById(machine.getId());
                if (Objects.nonNull(existingMcn) && !existingMcn.isActive()) {
                    errorMap.put(String.format("orderProducts[%s].companyYarnOrderProduct.machine",i),
                            new String[]{"UnderMaintenance.orderProductMappingDto.orderProducts.companyYarnOrderProduct.machine"});
                }
            } else if (StringUtils.isNotEmpty(machine.getMachineNo())) {
                List<Machine> existingMcn = productMappingService.findMachineByMachineNo(machine.getMachineNo().trim());
                if (isEmpty(existingMcn)) {
                    errorMap.put(String.format("orderProducts[%s].companyYarnOrderProduct.machine",i),
                            new String[]{"MachineNotFound.orderProductMappingDto.orderProducts.companyYarnOrderProduct.machine"});
                } else if (!existingMcn.get(0).isActive()) {
                    errorMap.put(String.format("orderProducts[%s].companyYarnOrderProduct.machine",i),
                            new String[]{"UnderMaintenance.orderProductMappingDto.orderProducts.companyYarnOrderProduct.machine"});
                } else {
                    orderProducts.get(i).getCompanyYarnOrderProduct().setMachine(existingMcn.get(0));
                }
            }
        }

        logSuffix += createNameValue("errorMap count",errorMap.size());
        log.info(createExitLog(logPrefix, logSuffix));
    }
}
