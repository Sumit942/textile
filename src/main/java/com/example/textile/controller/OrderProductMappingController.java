package com.example.textile.controller;

import com.example.textile.action.OrderProductMappingAction;
import com.example.textile.constants.ParameterKey;
import com.example.textile.dto.ErrorResponseDto;
import com.example.textile.dto.OrderProductMappingDto;
import com.example.textile.entity.OrderProductMapping;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.executors.RestActionExecutor;
import com.example.textile.service.OrderProductMappingService;
import com.example.textile.utility.FactoryUtility;
import com.example.textile.utility.factory.ActionExecutorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.textile.utility.LogUtils.*;

@AllArgsConstructor
@RestController
@RequestMapping("orderProductMapping")
@Slf4j
public class OrderProductMappingController extends BaseController {
    OrderProductMappingService productMappingService;

    public OrderProductMappingController(OrderProductMappingService productMappingService) {
        this.productMappingService = productMappingService;
    }

    Map<String , ActionExecutor> actionExecutorMap;

    @PostConstruct
    public void init() {
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(OrderProductMappingController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new OrderProductMappingAction(productMappingService));
    }


    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        OrderProductMapping byId = productMappingService.findById(id);
        if (Objects.isNull(byId)) {
            ErrorResponseDto errorResponseDto = ErrorResponseDto
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .errorMessages(Map.of("id","orderProductMapping not found by id="+id))
                    .errorDateTime(LocalDateTime.now())
                    .build();
            return ResponseEntity.badRequest().body(errorResponseDto);
        }
        return ResponseEntity.ok(byId);
    }

    @PostMapping("submit")
    public ResponseEntity<?> submit(@RequestBody OrderProductMappingDto productMappingDto, HttpServletRequest request) {
        //TODO: submit action
        String logPrefix = "submit()";
        log.info(createEntryLog(logPrefix));
        String logSuffix = createNameValue("productMappingDto", productMappingDto.getId());

        RestActionExecutor actionExecutor = (RestActionExecutor<OrderProductMappingDto>) actionExecutorMap.get(ActionType.SUBMIT.getActionType());
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ParameterKey.ACTION, ActionType.SUBMIT);
        Map<String, String[]> errorMap = new HashMap<>();
        try {
            ActionResponse<OrderProductMappingDto> actionResponse = actionExecutor.executeRest(productMappingDto, parameterMap, errorMap);
            if (ResponseType.SUCCESS.equals(actionResponse.getResponseType())) {
                return ResponseEntity.created(URI.create("/"+ productMappingDto.getId())).body(actionResponse.getDbObj());
            } else {
                ErrorResponseDto validationErrorResponseDto = ErrorResponseDto.builder()
                        .errorMessages(FactoryUtility.convertToErrorMsg(errorMap, messageSource, request))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .errorDateTime(LocalDateTime.now())
                        .build();
                log.info(createExitLog(logPrefix, logSuffix));
                return ResponseEntity.badRequest().body(validationErrorResponseDto);
            }
        } catch (Exception e) {
            log.error("Exception while submit() :{}", e.getLocalizedMessage(), e);
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                    .errorMessages(Map.of("SystemError", messageSource.getMessage("System.Error", null, request.getLocale())))
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorDateTime(LocalDateTime.now())
                    .build();
            log.info(createExitLog(logPrefix, logSuffix));
            return ResponseEntity.internalServerError().body(errorResponseDto);
        }

    }

}
