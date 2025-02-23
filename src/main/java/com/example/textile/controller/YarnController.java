package com.example.textile.controller;

import com.example.textile.action.YarnSubmitAction;
import com.example.textile.dto.ErrorResponseDto;
import com.example.textile.dto.YarnDto;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.ServiceActionException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.executors.RestActionExecutor;
import com.example.textile.service.YarnService;
import com.example.textile.utility.FactoryUtility;
import com.example.textile.utility.ShreeramTextileConstants;
import com.example.textile.utility.factory.ActionExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/yarn")
public class YarnController extends BaseController {

    private final YarnService yarnService;

    Map<String, ActionExecutor> actionExecutorMap;

    public YarnController(YarnService yarnService) {
        this.yarnService = yarnService;
    }

    @PostConstruct
    public void init() {
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(YarnController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new YarnSubmitAction(yarnService));
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        //TODO: to verify if works for @RequestBody
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping
    public ResponseEntity<List<YarnDto>> fetchAllYarns() {
        List<YarnDto> yarnDtos = yarnService.findAll();
        return new ResponseEntity<>(yarnDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<YarnDto> getYarnById(@PathVariable Long id) {
        YarnDto yarnDto = yarnService.findById(id);
        if (yarnDto != null) {
            return new ResponseEntity<>(yarnDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody YarnDto yarnDto, HttpServletRequest request) throws ServiceActionException {
        String logPrefix = "save() |";
        log.info("{} Entry ", logPrefix);
        RestActionExecutor<YarnDto> actionExecutor = (RestActionExecutor<YarnDto>) actionExecutorMap.get(ActionType.SUBMIT.getActionType());
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);


        try {
            Map<String, String[]> errorMap = new HashMap<>();
            ActionResponse actionResponse = actionExecutor.executeRest(yarnDto, parameterMap, errorMap);
            if (ResponseType.SUCCESS.equals(actionResponse.getResponseType())) {

                return new ResponseEntity<>(actionResponse.getDbObj(), HttpStatus.CREATED);
            } else {
                log.error("{} Error ", logPrefix);
                ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                        .errorMessages(FactoryUtility.convertToErrorMsg(errorMap, messageSource, request))
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .errorDateTime(LocalDateTime.now())
                        .build();

                return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error("Exception: save() " + e.getLocalizedMessage(), e);
            ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                    .errorMessages(Map.of("SystemError", messageSource.getMessage("System.Error", null, request.getLocale())))
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorDateTime(LocalDateTime.now())
                    .build();
            return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }

    @GetMapping("searchBy")
    public ResponseEntity<List<YarnDto>> searchByName(@RequestParam String type) {
        try {
            return ResponseEntity.ok(yarnService.findByTypeLike(type));
        } catch (Exception e) {
            log.error("error while searching type"+type, e);
            return ResponseEntity.ok(List.of());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<YarnDto> patch(@PathVariable Long id, @RequestBody YarnDto yarnDto) {
        YarnDto dto = yarnService.updateYarn(id, yarnDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteYarn(@PathVariable Long id) {
        yarnService.deleteYarn(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
