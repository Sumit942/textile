package com.example.textile.controller;

import com.example.textile.action.ProductSubmitAction;
import com.example.textile.command.ProductCommand;
import com.example.textile.constants.CommandConstants;
import com.example.textile.constants.TextileConstants;
import com.example.textile.entity.Product;
import com.example.textile.enums.ActionType;
import com.example.textile.enums.ResponseType;
import com.example.textile.executors.ActionExecutor;
import com.example.textile.executors.ActionResponse;
import com.example.textile.service.ProductRateService;
import com.example.textile.service.ProductService;
import com.example.textile.utility.ShreeramTextileConstants;
import com.example.textile.utility.factory.ActionExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController{

    @Autowired
    ProductService productService;

    @Autowired
    ProductRateService productRateService;

    Map<String, ActionExecutor> actionExecutorMap;

    @PostConstruct
    public void init() {
        actionExecutorMap = ActionExecutorFactory.getFactory().getActionExecutors(ProductController.class);
        actionExecutorMap.put(ActionType.SUBMIT.getActionType(), new ProductSubmitAction(productService));
    }

    @GetMapping("/searchByName")
    @ResponseBody
    public List<Product> searchByName(@RequestParam("name") String name,@RequestParam( value = "active", required = false) Boolean active) {
        return productService.findByNameAndLimit(name,10, active);
    }

    @GetMapping("/rateByProductAndCompanyId")
    @ResponseBody
    public Double getProductRateByCompanyId(@RequestParam(required = false) Long companyId, @RequestParam Long productId) {
        return productRateService.getRateByCompanyAndProduct(companyId, productId);
    }

    @GetMapping
    public String showProduct(@ModelAttribute(CommandConstants.PRODUCT_COMMAND) ProductCommand command,
                              BindingResult result, ModelMap model, RedirectAttributes redirectAttr) {
        log.info("showProduct() Entry");
        return "/product";
    }

    @PostMapping
    public String saveProduct(@ModelAttribute(CommandConstants.PRODUCT_COMMAND) ProductCommand command,
                                    BindingResult result, ModelMap model, RedirectAttributes redirectAttr,
                                    @RequestParam(value = "searchProduct", required = false) String searchProduct,
                                    @RequestParam(value = "saveProduct", required = false) String saveProduct,
                                    HttpServletRequest request) {
        String logPrefix = "submit() |";
        log.info("{} Entry",logPrefix);
        String view = "redirect:/product";

        if (searchProduct != null) {
            log.info("{} Inside searchProduct",logPrefix);
            Product byId = productService.findById(command.getSearchProduct().getId());
            command.setProduct(byId);
        } else {
            log.info("{} Inside saveProduct",logPrefix);
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put(ShreeramTextileConstants.ACTION, ActionType.SUBMIT);
            parameterMap.put(TextileConstants.USER, getLoggedInUser());

            ActionExecutor actExecutor = actionExecutorMap.get(ActionType.SUBMIT.getActionType());
            ActionResponse response = null;
            try {
                response = actExecutor.execute(command, parameterMap, result, model);
                if (ResponseType.SUCCESS.equals(response.getResponseType())) {
                    log.info("{} saved Successfully!!", logPrefix);
                    redirectAttr.addFlashAttribute("successMessage",
                            messageSource.getMessage("ActionResponse.Success.Submit.Product",
                                    new Object[]{command.getProduct().getName()},
                                    request.getLocale()));
                    command.setProduct(null);
                } else {
                    log.error("result has doValidation Errors");
                    result.getAllErrors().forEach(System.out::println);
                    log.info("{} save Unsuccessfull", logPrefix);
                    view = "/product";
                }
            } catch (Throwable e) {
                log.error("Error while saving product-" + e.getLocalizedMessage(), e);
                view = "/product";
            }
            redirectAttr.addFlashAttribute(TextileConstants.ACTION_RESPONSE,response);
        }
        redirectAttr.addFlashAttribute(CommandConstants.PRODUCT_COMMAND, command);

        return view;
    }
}
