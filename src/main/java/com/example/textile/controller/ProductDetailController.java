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
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/productDetail")
public class ProductDetailController extends BaseController{

    @Autowired private ProductDetailService productDetailService;

    Map<String, ActionExecutor> actionExecutorMap;

    private static final String YARN_RETURN = "%YARN RETURN";

    @PostConstruct
    public void init() {
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(ProductDetailController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new ProductDetailSubmitAction(productDetailService));
    }

    @GetMapping
    public String showForm(@ModelAttribute(CommandConstants.PRODUCT_DETAILS_COMMAND) ProductDetailCommand command,
                           Model model,
                           @RequestParam(value ="searchByCh", required = false) Long chNo) throws InvalidObjectPopulationException {
        if (chNo != null && chNo.compareTo(0L) > 0) {
            log.debug("showForm() searchByCh");
            List<ProductDetail> byChNo = productDetailService.findByChNo(chNo);
            command.setProductDetails(byChNo);
        }

        ActionExecutor actionExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());
        actionExecutor.prePopulateOptionsAndFields(command, model);

        return "/productDetails";
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
                    view = "/productDetails";
                }
                redirectAttr.addFlashAttribute("actionResponse", response);
            } catch (Throwable e) {
                log.error("Error while saving productDetails-" + e.getLocalizedMessage(), e);
                view = "/productDetails";
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
//        List<Long> unBilledChNo = productDetailService.findAllChNoAndInvoice_IsNull();
        List<ProductDetail> unBilledChNo = productDetailService.findAllByInvoice_Is_Null_And_productName_Not_EndsWith(YARN_RETURN);
        List<ProductDetail> yarnReturnChNo = productDetailService.findAllByInvoice_Is_Null_And_productName_EndsWith(YARN_RETURN);

        if (!allChNo.isEmpty()){
            long min = allChNo.stream().min(Long::compareTo).orElse(0L);
            long max = allChNo.stream().max(Long::compareTo).orElse(-1L);

            for (long i = min; i <= max; i++)
                if (!allChNo.contains(i))
                    missingChNos.add(i);

            model.addAttribute("missingChallanNos", missingChNos);
            model.addAttribute("unBilledChNo", unBilledChNo);
            model.addAttribute("yarnReturnChNo", yarnReturnChNo);
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
        if (invoiceId != null)
            return productDetailService.findByPartyIdAndInvoiceId(partyId, invoiceId);
        return productDetailService.findByPartyIdAndProductName_Not_Like(partyId, YARN_RETURN);
    }

    @GetMapping("/searchByChallanNo")
    @ResponseBody
    public List<ProductDetail> findByChallanNo(@RequestParam("chNo") Long chNo) {
        return productDetailService.findByChNo(chNo);
    }
}
