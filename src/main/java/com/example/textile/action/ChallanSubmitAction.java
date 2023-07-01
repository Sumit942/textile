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

import java.util.HashMap;
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
        Challan challan = challanCommand.getChallan();
        String logPrefix = "doValidation() |";
        String logSuffix = "";
        log.info("{} Entry", logPrefix);
        Map<String,String> errMap = new HashMap<>();

        validateChallan(challan, errMap);

        errMap.forEach(result::rejectValue);
        log.info("{} Exit [{}]", logPrefix, logSuffix);

    }

    protected void validateChallan(Challan challan, Map<String,String> errMap) {
        if (challan.getChDate() == null) {
            errMap.put("challanCommand.chDate","NotNull.challanCommand.chDate");
        }
        if (challan.getChallanNo() == null || challan.getChallanNo().compareTo(0L) <= 0) {
            errMap.put("challanCommand.challanNo","NotNull.challanCommand.challanNo");
        }
//        if (challan.getType() == null || challan.getType().isEmpty()) {
//            errMap.put("challanCommand.type","NotNull.challanCommand.type");
//        }
        if (challan.getPartyName() == null || challan.getPartyName().getId() == null
                || challan.getPartyName().getId().compareTo(0L) <= 0) {
            errMap.put("challanCommand.partyName","NotNull.challanCommand.partyName");
        }
        /*if (challan.getDeliveryAddress() == null || challan.getDeliveryAddress().isEmpty()) {
            errMap.put("challanCommand.deliveryAddress","NotNull.challanCommand.deliveryAddress");
        }
        if (challan.getTransportName() == null || challan.getTransportName().isEmpty()) {
            errMap.put("challanCommand.transportName","NotNull.challanCommand.transportName");
        }*/
        if (challan.getYarn() == null | challan.getYarn().isEmpty()) {
            errMap.put("challanCommand.yarn","NotNull.challanCommand.yarn");
        } else {
            for (int i = 0; i < challan.getYarn().size(); i++) {
                Yarn yarn = challan.getYarn().get(i);
                if (yarn.getId() == null || yarn.getId().compareTo(0L) <= 0) {
                    errMap.put("challanCommand.yarn[0]","NotNull.challanCommand.yarn");
                }
            }
        }
//        if (challan.getGsm() == null || challan.getGsm().isEmpty()) {
//            errMap.put("challanCommand.gsm","NotNull.challanCommand.gsm");
//        }
        if (challan.getMachine().getId() == null || challan.getMachine().getId().compareTo(0L) <= 0) {
            errMap.put("challanCommand.machine","NotNull.challanCommand.machine");
        }
        if (challan.getFinishDia() == null || challan.getFinishDia().isEmpty()) {
            errMap.put("challanCommand.finishDia","NotNull.challanCommand.finishDia");
        }
        if (challan.getFabricDesign().getId() == null || challan.getFabricDesign().getId().compareTo(0L) <= 0) {
            errMap.put("challanCommand.fabricDesign","NotNull.challanCommand.fabricDesign");
        }
        if (challan.getQuantity() == null || challan.getQuantity().compareTo(0.0) <= 0) {
            errMap.put("challanCommand.quantity","NotNull.challanCommand.quantity");
        }
        //TODO: Roll
        if (challan.getRate() == null || challan.getRate().compareTo(0.0) <= 0) {
            errMap.put("challanCommand.rate","NotNull.challanCommand.rate");
        }

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
        log.info("{} Exit", logPrefix);
    }
}
