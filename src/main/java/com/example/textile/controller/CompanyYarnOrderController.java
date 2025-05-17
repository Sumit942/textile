package com.example.textile.controller;

import com.example.textile.action.CompanyYarnOrderSubmitAction;
import com.example.textile.dto.CompanyYarnOrderDto;
import com.example.textile.dto.ErrorResponseDto;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.executors.RestActionExecutor;
import com.example.textile.service.CompanyYarnOrderService;
import com.example.textile.service.YarnService;
import com.example.textile.utility.FactoryUtility;
import com.example.textile.utility.ShreeramTextileConstants;
import com.example.textile.utility.factory.ActionExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.textile.utility.LogUtils.*;

@Slf4j
@RestController
@RequestMapping("companyYarnOrder")
public class CompanyYarnOrderController extends BaseController {

    private final CompanyYarnOrderService yarnOrderService;
    private final YarnService yarnService;
    public CompanyYarnOrderController(CompanyYarnOrderService yarnOrderService, YarnService yarnService) {
        this.yarnOrderService = yarnOrderService;
        this.yarnService = yarnService;
    }

    Map<String, ActionExecutor> actionExecutorMap;

    @PostConstruct
    public void init() {
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(CompanyYarnOrderController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new CompanyYarnOrderSubmitAction(yarnOrderService, yarnService));
    }


    @PostMapping("submit")
    public ResponseEntity<?> submit(@RequestBody CompanyYarnOrderDto yarnOrderDto, HttpServletRequest request) {
        String logPrefix = "submit()";
        String logSuffix = createNameValue("yarnOrderDtoId", yarnOrderDto.getId());
        log.info(createEntryLog(logPrefix));

        Map<String, String[]> errorMap = new HashMap<>();
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);
        RestActionExecutor<CompanyYarnOrderDto> actionExecutor = (RestActionExecutor<CompanyYarnOrderDto>) actionExecutorMap.get(ActionType.SUBMIT.getActionType());
        try {
            ActionResponse<CompanyYarnOrderDto> actionResponse = actionExecutor.executeRest(yarnOrderDto, parameterMap, errorMap);
            if (actionResponse.getResponseType().equals(ResponseType.SUCCESS)) {
                log.info(createExitLog(logPrefix, logSuffix));
                return new ResponseEntity<>(actionResponse.getDbObj(), HttpStatus.CREATED);
            } else {
                ErrorResponseDto validationErrorResponseDto = ErrorResponseDto.builder()
                        .errorMessages(FactoryUtility.convertToErrorMsg(errorMap, messageSource, request))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .errorDateTime(LocalDateTime.now())
                        .build();
                log.info(createExitLog(logPrefix, logSuffix));
                return new ResponseEntity<>(validationErrorResponseDto, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Error while CompanyYarnOrderDto submit: " + e.getLocalizedMessage(), e);
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                    .errorMessages(Map.of("SystemError", messageSource.getMessage("System.Error", null, request.getLocale())))
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorDateTime(LocalDateTime.now())
                    .build();

            log.info(createExitLog(logPrefix, logSuffix));
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<CompanyYarnOrderDto>> fetchYarnOrders(@RequestParam(defaultValue = "10", name = "size") Long size) {
        List<CompanyYarnOrderDto> yarnOrderDtos = yarnOrderService.findAllCompanyYarnOrderDto(size);
        return ResponseEntity.ok(yarnOrderDtos);
    }
}
