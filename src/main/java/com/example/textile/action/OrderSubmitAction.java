package com.example.textile.action;

import com.example.textile.constants.TextileConstants;
import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.Orders;
import com.example.textile.entity.User;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.executors.ActionResponse;
import com.example.textile.executors.RestActionExecutor;
import com.example.textile.service.OrdersService;
import com.example.textile.utility.ShreeramTextileConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class OrderSubmitAction extends RestActionExecutor<OrdersDto> {
    private OrdersService ordersService;

    @Override
    protected ActionResponse onSuccessRest(OrdersDto ordersDto, Map<String, Object> parameterMap) {
        log.debug("Entry");
        ActionType action = (ActionType) parameterMap.get(ShreeramTextileConstants.ACTION);
        //adding logged in user to entity for audit purpose
        User user = (User) parameterMap.get(TextileConstants.USER);
        ordersDto.setUser(user);
        OrdersDto saveOrder = ordersService.saveOrUpdate(ordersDto);
        ActionResponse actionResponse = new ActionResponse(ResponseType.SUCCESS);
        actionResponse.setDbObj(saveOrder);
        log.info("Exit [action={}, id={}]", action.getActionType(), saveOrder.getId());

        return actionResponse;
    }

    @Override
    protected void doValidationRest(OrdersDto ordersDto, Map<String, Object> parameterMap, Map<String, String> errorMap) {
        log.debug("Entry");

    }

    @Override
    protected void doPreSaveOperationRest(OrdersDto ordersDto, Map<String, Object> parameterMap, Map<String, String> errorMap) {
        if (Objects.nonNull(ordersDto.getId()) && ordersDto.getId().compareTo(0L) > 0) {
            OrdersDto order = ordersService.findById(ordersDto.getId());
            //TODO: order pre-save operation eg. company bank detail hibernate orphanRemoval check
        }
    }
}
