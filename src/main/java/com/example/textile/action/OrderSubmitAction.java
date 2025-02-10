package com.example.textile.action;

import com.example.textile.constants.TextileConstants;
import com.example.textile.dto.CompanyYarnOrderDto;
import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.User;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.executors.ActionResponse;
import com.example.textile.executors.RestActionExecutor;
import com.example.textile.service.CompanyYarnOrderService;
import com.example.textile.service.OrdersService;
import com.example.textile.utility.ShreeramTextileConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class OrderSubmitAction extends RestActionExecutor<OrdersDto> {
    private OrdersService ordersService;
    private CompanyYarnOrderService yarnOrderService;

    @Override
    protected ActionResponse onSuccessRest(OrdersDto ordersDto, Map<String, Object> parameterMap) {
        log.debug("Entry");
        ActionType action = (ActionType) parameterMap.get(ShreeramTextileConstants.ACTION);
        //adding logged in user to entity for audit purpose
        User user = (User) parameterMap.get(TextileConstants.USER);
        ordersDto.setUser(user);
        OrdersDto saveOrder = ordersService.save(ordersDto);
        ActionResponse actionResponse = new ActionResponse(ResponseType.SUCCESS);
        actionResponse.setDbObj(saveOrder);
        log.info("Exit [action={}, id={}]", action.getActionType(), saveOrder.getId());

        return actionResponse;
    }

    @Override
    protected void doValidationRest(OrdersDto ordersDto, Map<String, Object> parameterMap, Map<String, String[]> errorMap) {
        String logPrefix = " doValidationRest()";
        log.debug("Entry{}", logPrefix);

        List<CompanyYarnOrderDto> yarnOrders = ordersDto.getCompanyYarnOrders();
        if (Objects.nonNull(yarnOrders) && !yarnOrders.isEmpty()) {
            List<String> yarnInvoiceNos = new ArrayList<>();
            for (int i = 0; i < yarnOrders.size(); i++) {
                if (Objects.isNull(yarnOrders.get(i).getYarnInvoiceNo()) || yarnOrders.get(i).getYarnInvoiceNo().isBlank())
                    continue;
                if (yarnInvoiceNos.contains(yarnOrders.get(i).getYarnInvoiceNo())) {
                    errorMap.put("yarnOrders["+i+"].yarnInvoiceNo", new String[]{"Duplicate.ordersDto.yarnOrders.yarnInvoiceNo"});
                } else {
                    yarnInvoiceNos.add(yarnOrders.get(i).getYarnInvoiceNo());
                }
            }
            if (errorMap.isEmpty()) {
                //checking the yarnInvoiceNo in db if no validation errors
                for (int i = 0; i < yarnOrders.size(); i++) {
                    List<Long> orderIds = yarnOrderService.getOrderIdByYarnInvoiceNoAndIdNot(yarnOrders.get(i).getYarnInvoiceNo(), ordersDto.getId());
                    if (!orderIds.isEmpty()) {
                        errorMap.put("yarnOrders["+i+"].yarnInvoiceNo", new String[]{"DBDuplicate.ordersDto.yarnOrders.yarnInvoiceNo",
                                orderIds.toString()});
                    }
                }
            }
        }
    }

    @Override
    protected void doPreSaveOperationRest(OrdersDto ordersDto, Map<String, Object> parameterMap, Map<String, String[]> errorMap) {

    }
}
