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
import com.example.textile.service.ProductDetailService;
import com.example.textile.utility.ShreeramTextileConstants;
import com.example.textile.utility.factory.ActionExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/productDetail")
public class ProductDetailController extends BaseController{

    @Autowired private ProductDetailService productDetailService;

    Map<String, ActionExecutor> actionExecutorMap;

    @Value("${challan.showGroupByParty}") private Boolean showGroupByParty;

    @PostConstruct
    public void init() {
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(ProductDetailController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new ProductDetailSubmitAction(productDetailService));
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
        CustomDateEditor dateEditor = new CustomDateEditor(ShreeramTextileConstants.SIMPLE_DATE_FORMAT_ddMMYYYY_SLASH, true);
        dataBinder.registerCustomEditor(Date.class, dateEditor);
    }

    @GetMapping
    public String showForm(@ModelAttribute(CommandConstants.PRODUCT_DETAILS_COMMAND) ProductDetailCommand command,
                           Model model,
                           @RequestParam(value ="searchByCh", required = false) Long chNo,
                           HttpServletRequest request) throws InvalidObjectPopulationException {
        String userAgent = request.getHeader(ShreeramTextileConstants.USER_AGENT);
        String showChallanForm = userAgent.contains("Mobile") ? "/productDetails_mobile" : "/productDetails";
        if (chNo != null && chNo.compareTo(0L) > 0) {
            log.debug("showForm() searchByCh");
            List<ProductDetail> byChNo = productDetailService.findByChNo(chNo);
            command.setProductDetails(byChNo);
        }

        ActionExecutor actionExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());
        actionExecutor.prePopulateOptionsAndFields(command, model);

        return showChallanForm;
    }

    @PostMapping
    public String submit(@ModelAttribute(CommandConstants.PRODUCT_DETAILS_COMMAND) ProductDetailCommand command,
                         BindingResult result, ModelMap model, RedirectAttributes redirectAttr,
                         @RequestParam(value = "searchChallans", required = false) String searchChallans,
                         @RequestParam(value = "saveChallans", required = false) String saveChallans,
                         HttpServletRequest request) {

        String logPrefix = "submit() |";
        log.info("{} Entry",logPrefix);
        String view = "redirect:/productDetail";

        if (searchChallans != null) {
            log.info("{} Inside searchChallans",logPrefix);
            List<ProductDetail> challanList = productDetailService.challanReport(command);
            command.setProductDetails(challanList);
        } else if (saveChallans != null) {
            String userAgent = request.getHeader(ShreeramTextileConstants.USER_AGENT);
            log.info("{} Inside saveChallans",logPrefix);
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);
            parameterMap.put(TextileConstants.USER, getLoggedInUser());

            ActionExecutor actExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());
            ActionResponse response;
            try {
                response = actExecutor.execute(command, parameterMap, result, model);
                if (ResponseType.SUCCESS.equals(response.getResponseType())) {
                    log.info("{} saved Successfully!!", logPrefix);
                    redirectAttr.addFlashAttribute("successMessage",
                            messageSource.getMessage("ActionResponse.Success.Submit.Challans",
                                    new Object[]{command.getProductDetails().stream().map(ProductDetail::getChNo).collect(Collectors.toList()).toString()},
                                    request.getLocale()));
                    command.setProductDetails(null); //set this null for empty row in jsp
                } else {
                    log.error("result has doValidation Errors");
                    result.getAllErrors().forEach(System.out::println);
                    log.info("{} save Unsuccessfull", logPrefix);
                    view = userAgent.contains("Mobile") ? "/productDetails_mobile" : "/productDetails";
                }
                redirectAttr.addFlashAttribute("actionResponse", response);
            } catch (Throwable e) {
                log.error("Error while saving productDetails-" + e.getLocalizedMessage(), e);
                view = userAgent.contains("Mobile") ? "/productDetails_mobile" : "/productDetails";
            }
        }
        redirectAttr.addFlashAttribute(CommandConstants.PRODUCT_DETAILS_COMMAND, command);

        return view;
    }

    @GetMapping("/missingChallans")
    public String challanOverview(ModelMap model) {
        String logPrefix = "challanOverview() ";
        log.info("{} Entry",logPrefix);
        List<Long> missingChNos = new ArrayList<>();

        List<Long> allChNo = productDetailService.findAllChNo();
        List<ProductDetail> unBilledChNo = productDetailService.findAllUnbilledByPartyId(null,null);
        List<ProductDetail> allExcludedCh = productDetailService.findAllExcluded();

        if (showGroupByParty) {
            Map<String, List<ProductDetail>> unBilledChNoByPartyName = unBilledChNo.stream()
                    .collect(Collectors.groupingBy(productDetail -> productDetail.getParty().getName()));
            Map<String, List<ProductDetail>> yarnReturnChNoByPartyName = allExcludedCh.stream()
                    .collect(Collectors.groupingBy(productDetail -> productDetail.getParty().getName()));
            model.addAttribute("unBilledChNoByPartyName", unBilledChNoByPartyName);
            model.addAttribute("yarnReturnChNoByPartyName", yarnReturnChNoByPartyName);
            model.addAttribute("showGroupByParty", showGroupByParty);
        }   else {
            model.addAttribute("unBilledChNo", unBilledChNo);
            model.addAttribute("allExcludedChNo", allExcludedCh);
        }
        if (!allChNo.isEmpty()){
            long min = allChNo.get(0);
            long max = allChNo.get(allChNo.size()-1);

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
                                            @RequestParam(value = "invoiceId",required = false) Long invoiceId,
                                            @RequestParam(value = "challans") List<Long> challans)  {
        if (invoiceId != null)
            return productDetailService.findByPartyIdAndInvoiceId(partyId, invoiceId);
        return productDetailService.findAllUnbilledByPartyId(partyId, challans);
    }

    @GetMapping("/searchByChallanNo")
    @ResponseBody
    public List<ProductDetail> findByChallanNo(@RequestParam("chNo") Long chNo) {
        return productDetailService.findByChNo(chNo);
    }
}
