package com.example.textile.controller;

import com.example.textile.action.YarnFabricDesignAction;
import com.example.textile.dto.ErrorResponseDto;
import com.example.textile.entity.YarnFabricDesign;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.executors.RestActionExecutor;
import com.example.textile.service.YarnFabricDesignService;
import com.example.textile.utility.FactoryUtility;
import com.example.textile.utility.ShreeramTextileConstants;
import com.example.textile.utility.factory.ActionExecutorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.textile.utility.LogUtils.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("yarnFabricDesign")
public class YarnFabricDesignController extends BaseController{
    private final YarnFabricDesignService yarnFabricDesignService;

    Map<String, ActionExecutor> actionExecutorMap;

    @PostConstruct
    public void init() {
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(YarnFabricDesignController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new YarnFabricDesignAction(yarnFabricDesignService));
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submit(@RequestBody YarnFabricDesign yarnFabricDesign, HttpServletRequest request) {
        String logPrefix = "submit()";
        String logSuffix = createLogSuffix("yarnFabricDesign id", yarnFabricDesign.getId());
        log.info(createEntryLog(logPrefix));

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);
        Map<String, String[]> errorMap = new HashMap<>();
        RestActionExecutor<YarnFabricDesign> actionExecutor = (RestActionExecutor<YarnFabricDesign>) actionExecutorMap.get(ActionType.SUBMIT.getActionType());
        try {
            ActionResponse<YarnFabricDesign> actionResponse = actionExecutor.executeRest(yarnFabricDesign, parameterMap, errorMap);
            if (actionResponse.getResponseType().equals(ResponseType.SUCCESS)) {
                log.info(createExitLog(logPrefix, logSuffix));
                return ResponseEntity.created(URI.create("/"+yarnFabricDesign.getId())).body(yarnFabricDesign);
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
            log.error("Exception while YarnFabricDesign submit() :" + e.getLocalizedMessage(), e);
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
