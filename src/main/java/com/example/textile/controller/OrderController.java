package com.example.textile.controller;

import com.example.textile.action.OrderSubmitAction;
import com.example.textile.constants.ParameterKey;
import com.example.textile.dto.ErrorResponseDto;
import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.Orders;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.executors.RestActionExecutor;
import com.example.textile.service.CompanyYarnOrderService;
import com.example.textile.service.OrdersService;
import com.example.textile.utility.FactoryUtility;
import com.example.textile.utility.factory.ActionExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.textile.transform.TransformationEntityToDTO.transformOrdersEntity;

@Slf4j
@RestController
@RequestMapping("order")
public class OrderController extends BaseController{

    private final OrdersService ordersService;
    private final CompanyYarnOrderService yarnOrderService;
    private final ModelMapper modelMapper;

    Map<String ,ActionExecutor> actionExecutorMap;

    public OrderController(OrdersService ordersService, CompanyYarnOrderService yarnOrderService, ModelMapper modelMapper) {
        this.ordersService = ordersService;
        this.yarnOrderService = yarnOrderService;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init() {
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(OrderController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new OrderSubmitAction(ordersService,yarnOrderService));
    }

    @GetMapping //TODO: restrict to only admin
    public ResponseEntity<List<OrdersDto>> fetchAll() {
        log.info("Entry fetching all orders");
        List<OrdersDto> ordersDtos = ordersService.findAll();
        return new ResponseEntity<>(ordersDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrdersDto> saveOrder(@RequestBody OrdersDto ordersDto) {
        log.info("Entry saving Order");

        Orders savedOrder = ordersService.save(ordersDto);
        return new ResponseEntity<>(transformOrdersEntity(modelMapper, savedOrder), HttpStatus.CREATED);
    }

    @PostMapping("submit")
    public ResponseEntity<?> submitOrder(@RequestBody OrdersDto ordersDto, HttpServletRequest request) {

        RestActionExecutor<OrdersDto> actionExecutor = (RestActionExecutor<OrdersDto>) actionExecutorMap.get(ActionType.SUBMIT.getActionType());
        Map<String, Object> parameterMap = new HashMap<>();
        Map<String, String[]> errorMap = new HashMap<>();

        parameterMap.put(ParameterKey.ACTION, ActionType.SUBMIT);
        parameterMap.put(ParameterKey.MODEL_MAPPER, modelMapper);
        try {
            ActionResponse<OrdersDto> actionResponse = actionExecutor.executeRest(ordersDto, parameterMap, errorMap);
            if (actionResponse.getResponseType().equals(ResponseType.SUCCESS)) {
                return new ResponseEntity<>(actionResponse.getDbObj(), HttpStatus.CREATED);
            } else {
                ErrorResponseDto validationErrorResponseDto = ErrorResponseDto.builder()
                        .errorMessages(FactoryUtility.convertToErrorMsg(errorMap, messageSource, request))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .errorDateTime(LocalDateTime.now())
                        .build();
                return new ResponseEntity<>(validationErrorResponseDto, HttpStatus.BAD_REQUEST);
            }
        } catch (Throwable e) {
            log.error("Exception: submitOrder() " + e.getLocalizedMessage(), e);
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                    .errorMessages(Map.of("SystemError", messageSource.getMessage("System.Error", null, request.getLocale())))
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorDateTime(LocalDateTime.now())
                    .build();
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
