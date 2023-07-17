package com.example.textile.controller;

import com.example.textile.action.ProductDetailSubmitAction;
import com.example.textile.command.ProductDetailCommand;
import com.example.textile.constants.CommandConstants;
import com.example.textile.constants.TextileConstants;
import com.example.textile.entity.ProductDetail;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.exception.InvalidObjectPopulationException;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.repo.ProductDetailRepository;
import com.example.textile.service.ProductDetailService;
import com.example.textile.utility.ShreeramTextileConstants;
import com.example.textile.utility.factory.ActionExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/productDetail")
public class ProductDetailController extends BaseController{

    @Autowired private ProductDetailService productDetailService;

    Map<String, ActionExecutor> actionExecutorMap;

    @PostConstruct
    public void init() {
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(ProductDetailController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new ProductDetailSubmitAction(productDetailService));
    }

    @GetMapping()
    public String showForm(@ModelAttribute(CommandConstants.PRODUCT_DETAILS_COMMAND) ProductDetailCommand command,
                           Model model) throws InvalidObjectPopulationException {
        ActionExecutor actionExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());
        actionExecutor.prePopulateOptionsAndFields(command, model);

        return "/productDetails";
    }

    @PostMapping
    public String submit(@ModelAttribute(CommandConstants.PRODUCT_DETAILS_COMMAND) ProductDetailCommand command,
                         BindingResult result, ModelMap model, RedirectAttributes redirectAttr) {
        //TODO: saving stuffs
        String logPrefix = "submit() |";
        log.info("{} Entry",logPrefix);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);
        parameterMap.put(TextileConstants.USER,getLoggedInUser());

        ActionExecutor actExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());
        ActionResponse response;

        try {
            response = actExecutor.execute(command, parameterMap, result,model);
            if (ResponseType.SUCCESS.equals(response.getResponseType())) {
                log.info("{} saved Successfully!!", logPrefix);
                redirectAttr.addFlashAttribute("successMessage",
                        messageSource.getMessage("ActionResponse.Success.Submit",
                                new Object[]{
                                        command.getProductDetails().stream().map(ProductDetail::getChNo).collect(Collectors.toList()),
                                        command.getProductDetails().stream().map(ProductDetail::getId).collect(Collectors.toList())},
                                        Locale.ENGLISH));
            } else {
                redirectAttr.addFlashAttribute(CommandConstants.PRODUCT_DETAILS_COMMAND,command);
                log.error("result has doValidation Errors");
                result.getAllErrors().forEach(System.out::println);
                log.info("{} save Unsuccessfull", logPrefix);
            }
            redirectAttr.addFlashAttribute("actionResponse",response);
        } catch (Throwable e) {
            log.error("Error while saving productDetails-"+e.getLocalizedMessage(), e);
        }
        return "redirect:/productDetail";

    }

    @GetMapping("/missingChallans")
    public String challanOverview(ModelMap model) {
        String logPrefix = "challanOverview() ";
        log.info("{} Entry",logPrefix);
        List<Long> missingChNos = new ArrayList<>();

        List<Long> allChNo = productDetailService.findAllChNo().stream()
                .map(Long::parseLong).collect(Collectors.toList());

        if (!allChNo.isEmpty()){
            long min = allChNo.stream().min(Long::compareTo).orElse(0L);
            long max = allChNo.stream().max(Long::compareTo).orElse(-1L);

            for (long i = min; i <= max; i++)
                if (!allChNo.contains(i))
                    missingChNos.add(i);

            model.addAttribute("missingChallanNos", missingChNos);
            model.addAttribute("minChallanNo", min);
            model.addAttribute("maxChallanNo", max);
            log.info("{} Exit [min:{}, max:{}, missingCount:{}]", logPrefix, min, max, missingChNos.size());
        }

        return "/challanOverview";
    }

    @GetMapping("/getByParty")
    @ResponseBody
    public List<ProductDetail> getByPartyId(@RequestParam("partyId") Long partyId,
                                            @RequestParam(value = "invoiceId",required = false) Long invoiceId)  {
        return productDetailService.findByPartyIdAndInvoiceId(partyId, invoiceId);
    }
}
