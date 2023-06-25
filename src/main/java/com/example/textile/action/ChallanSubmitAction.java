package com.example.textile.action;

import com.example.textile.command.ChallanCommand;
import com.example.textile.constants.TextileConstants;
import com.example.textile.entity.*;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.ChallanService;
import com.example.textile.utility.ShreeramTextileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;

@Slf4j
public class ChallanSubmitAction extends ActionExecutor<ChallanCommand> {

    private final ChallanService challanService;

    public ChallanSubmitAction(ChallanService challanService) {
        this.challanService = challanService;
    }

    @Override
    protected ActionResponse onSuccess(ChallanCommand challanCommand, Map<String, Object> parameterMap, ModelMap model) {
        String logPrefix = "onSuccess() ";
        String logSufix = "";
        log.debug("{} Entry",logPrefix);
        ActionType action = (ActionType) parameterMap.get(ShreeramTextileConstants.ACTION);
        if (ActionType.SUBMIT.equals(action)) {
            Challan challan = challanCommand.getChallan();
            logSufix += challan.getChallanNo()+"; ";
            User user = (User) parameterMap.get(TextileConstants.USER);
            challan.setUser(user);
            Challan save = challanService.save(challan);
            logSufix += challan.getId() + "; SUBMIT= Success";
            challanCommand.setChallan(save);
        }

        ActionResponse actionResponse = new ActionResponse(ResponseType.SUCCESS);
        log.debug("{} Exit [{}]",logPrefix,logSufix);
        return actionResponse;
    }

    @Override
    protected void doValidation(ChallanCommand challanCommand, Map<String, Object> parameterMap, BindingResult result, ModelMap model) {

    }

    @Override
    protected void doPreSaveOperation(ChallanCommand challanCommand, BindingResult result) {

    }

    @Override
    public void prePopulateOptionsAndFields(ChallanCommand challanCommand, Object model) throws InvalidObjectPopulationException {
        String logPrefix = "populateOptionsAndFields() |";
        log.info("{} Entry", logPrefix);
        if(!(model instanceof ModelMap)) {
            throw new InvalidObjectPopulationException(challanCommand,model);
        }
        ModelMap modelView = (ModelMap) model;

        List<SaleType> saleTypes = challanService.getSaleTypeRepo().findAll();
        List<Machine> machines = challanService.getMachineRepo().findAll();
        List<Yarn> yarns = challanService.getYarnRepo().findAll();
        List<FabricDesign> fabricDesigns = challanService.getFabricDesignRepo().findAll();

        modelView.addAttribute("saleTypes",saleTypes);
        modelView.addAttribute("machines",machines);
        modelView.addAttribute("yarns",yarns);
        modelView.addAttribute("fabricDesigns",fabricDesigns);
    }
}
