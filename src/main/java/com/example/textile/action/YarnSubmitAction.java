package com.example.textile.action;

import com.example.textile.dto.YarnDto;
import com.example.textile.enums.ResponseType;
import com.example.textile.executors.ActionResponse;
import com.example.textile.executors.RestActionExecutor;
import com.example.textile.service.YarnService;
import com.example.textile.utility.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class YarnSubmitAction extends RestActionExecutor<YarnDto> {

    private final YarnService yarnService;

    public YarnSubmitAction(YarnService yarnService) {
        this.yarnService = yarnService;
    }

    @Override
    protected ActionResponse onSuccessRest(YarnDto yarnDto, Map<String, Object> parameterMap) {
        String logPrefix = "doSuccess() |";
        log.info("{} Entry", logPrefix);
        YarnDto persistedYarn = yarnService.save(yarnDto);

        ActionResponse actionResponse = new ActionResponse(ResponseType.SUCCESS);
        actionResponse.setDbObj(persistedYarn);
        log.info("{} Exit {}", logPrefix, actionResponse.getResponseType().getActResponse());
        return actionResponse;
    }

    @Override
    protected void doValidationRest(YarnDto yarnDto, Map<String, Object> parameterMap, Map<String, String> errorMap) {
        if (yarnDto.getType().isBlank()) {
            errorMap.put("type","NotBlank.yarnDto.type");
        } else {
            YarnDto yarnByType = yarnService.findByType(yarnDto.getType());
            if (Objects.nonNull(yarnByType) && yarnByType.getType().concat(yarnByType.getCompanyName()).equalsIgnoreCase(yarnDto.getType().concat(yarnByType.getCompanyName()))) {
                errorMap.put("type", "isDuplicate.yarnDto.type");
            }
        }
    }

    @Override
    protected void doPreSaveOperationRest(YarnDto yarnDto, Map<String, Object> parameterMap, Map<String, String> errorMap) {

    }
}
